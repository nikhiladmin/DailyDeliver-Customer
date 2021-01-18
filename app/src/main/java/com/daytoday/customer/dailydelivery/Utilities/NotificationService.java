package com.daytoday.customer.dailydelivery.Utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.daytoday.customer.dailydelivery.Network.ApiInterface;
import com.daytoday.customer.dailydelivery.Network.Client;
import com.daytoday.customer.dailydelivery.Network.Response.ChannelInformation;
import com.daytoday.customer.dailydelivery.Network.Response.RequestNotification;
import com.daytoday.customer.dailydelivery.Network.Response.SendDataModel;
import com.daytoday.customer.dailydelivery.Network.Response.YesNoResponse;
import com.daytoday.customer.dailydelivery.R;
import com.daytoday.customer.dailydelivery.WalkThrough.SplashScreenActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService extends FirebaseMessagingService {
    private NotificationManager notificationManager;
    public static final String TAG = "FirebaseMsgService";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.i(TAG,"Token is : " + token);
        SaveOfflineManager.setFireBaseToken(this,token);
        SaveOfflineManager.setFireBaseTokenChangedOrNot(this,true);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (remoteMessage.getData() != null) {
            SendDataModel data = getSendDataModelObject(remoteMessage.getData());
            PendingIntent pendingIntent = getIntent(this);
            ChannelInformation channelInformation = getChannelInformation(data);
            addChannel(channelInformation, NotificationManager.IMPORTANCE_DEFAULT);
            int notificationId = getNotificationID();
            notifyEveryOne(this,pendingIntent,data,channelInformation.getChannelId(),notificationId);
        }
    }

    private int getNotificationID() {
        //TODO Create A Notification ID So That we have a facility to cancel that Notification
        return 123456789;
    }

    private ChannelInformation getChannelInformation(SendDataModel data) {
        return new ChannelInformation("channelID","channelName","description of the channel");
    }

    private SendDataModel getSendDataModelObject(Map<String, String> data) {
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(data);
        SendDataModel sendDataModel = gson.fromJson(jsonElement, SendDataModel.class);
        return sendDataModel;
    }

    public void addChannel(ChannelInformation channel, int priorityOfChannel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel.getChannelId(), channel.getChannelName(), priorityOfChannel);
            notificationChannel.setDescription(channel.getChannelDescription());
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public PendingIntent getIntent(Context context) {
        //TODO return Pending Intent Here
        Intent intent = new Intent(context, SplashScreenActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        return pendingIntent;
    }

    public void notifyEveryOne(Context context, PendingIntent intent, SendDataModel sendDataModel, String channelId, int notificationId) {
        Notification notification = getNotification(context,intent,sendDataModel,channelId);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(notificationId,notification);
    }

    private Notification getNotification(Context context, PendingIntent intent,SendDataModel sendDataModel,String channelId) {
        return new NotificationCompat.Builder(context,"channel_id")
                .setContentTitle(getNotificationTitle(sendDataModel))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(getNotificationText(sendDataModel,20))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getNotificationText(sendDataModel)))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(intent)
                .setChannelId(channelId)
                .setAutoCancel(true)
                .build();
    }

    private String getNotificationTitle(SendDataModel sendDataModel) {
        //TODO Title needs to be added Here
        return "Title";
    }

    private CharSequence getNotificationText(SendDataModel sendDataModel, int noOfChars) {
        String notificationText = getNotificationText(sendDataModel);
        return notificationText.substring(0,Math.min(noOfChars,notificationText.length()));
    }

    private String getNotificationText(SendDataModel sendDataModel) {
        return "You Have A New Notification Here";
    }

    public static void sendNotification(RequestNotification requestNotification) {
        // Send Notification to FCM Server
        ApiInterface service = Client.getFirebaseClient().create(ApiInterface.class);
        sendNotificationToFCM(requestNotification, service);
        // Send Notification To Our Server
        service = Client.getClient().create(ApiInterface.class);
        SendDataModel sendDataModel = requestNotification.getSendDataModel();
        sendNotificationToOurServer(service, sendDataModel);
    }

    private static void sendNotificationToOurServer(ApiInterface service, SendDataModel sendDataModel) {
        Call<YesNoResponse> notificationCall = service.sendNotification(sendDataModel.getToWhichPersonId(),
                sendDataModel.getFromWhichPersonID(),
                Request.getRespectiveIntegerValue(sendDataModel.getNotificationStatus()),
                sendDataModel.getQuantity()
                ,sendDataModel.getProductName());
        notificationCall.enqueue(new Callback<YesNoResponse>() {
            @Override
            public void onResponse(Call<YesNoResponse> call, Response<YesNoResponse> response) {
                Log.i(TAG,"Response Successful " + response.body().getMessage());
            }

            @Override
            public void onFailure(Call<YesNoResponse> call, Throwable t) {
                Log.i(TAG,"Error Occurred :-> " + t.getMessage());
            }
        });
    }

    private static void sendNotificationToFCM(RequestNotification requestNotification, ApiInterface service) {
        Call<ResponseBody> messagingCall = service.postNotification(requestNotification);
        messagingCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Log.i(TAG,"Response is successful");
                }else Log.i(TAG,"Response is not successful Error is :-> " + response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG,"Response is not successful Error is :-> " + t.getMessage());
            }
        });
    }
}
