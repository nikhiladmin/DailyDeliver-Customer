package com.daytoday.customer.dailydelivery.HomeScreen.View;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.daytoday.customer.dailydelivery.LoginActivity.LoginPage;
import com.daytoday.customer.dailydelivery.Network.ApiInterface;
import com.daytoday.customer.dailydelivery.Network.Client;
import com.daytoday.customer.dailydelivery.Network.Response.YesNoResponse;
import com.daytoday.customer.dailydelivery.R;
import com.daytoday.customer.dailydelivery.Utilities.SaveOfflineManager;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class UserFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    MaterialTextView usertextview, userid;
    EditText usernameEditText, userphoneEditText, userAddress;
    MaterialButton button, signoutbutton;
    CircleImageView profileImg;
    String username;
    FloatingActionButton uploadProfile;
    private int GALLERY = 1, CAMERA = 2;
    String TAG = "UPLOADING";
    private FirebaseStorage storage;
    private StorageReference storageRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        View view = inflater.inflate(R.layout.fragment_user, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        profileImg = view.findViewById(R.id.profileImg);
        uploadProfile = view.findViewById(R.id.myacc_fab);
        usertextview = view.findViewById(R.id.UsersName);
        userid = view.findViewById(R.id.currId);
        usernameEditText = view.findViewById(R.id.myacc_name);
        userphoneEditText = view.findViewById(R.id.myacc_phone);
        userphoneEditText.setEnabled(false);
        userAddress = view.findViewById(R.id.myacc_address);
        button = view.findViewById(R.id.submitbutton);
        signoutbutton = view.findViewById(R.id.signout);
        username = firebaseAuth.getCurrentUser().getDisplayName().toUpperCase();
        usertextview.setText(SaveOfflineManager.getUserName(getContext()));
        userid.setText("ID-" + SaveOfflineManager.getUserId(getContext()));
        usernameEditText.setText(SaveOfflineManager.getUserName(getContext()));
        userphoneEditText.setText(SaveOfflineManager.getUserPhoneNumber(getContext()));
        userAddress.setText(SaveOfflineManager.getUserAddress(getContext()));


        setProfileImage();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getActivity());
                alertDialog.setMessage("This will be reflected in all the businesses you are connected.");
                alertDialog.setTitle("You are about to modify your profile details");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("I Understand", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUpload();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Profile Update Cancelled", Snackbar.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });
        signoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder alertDialog = new MaterialAlertDialogBuilder(getActivity());
                alertDialog.setMessage("Do you want to logout?");
                alertDialog.setTitle("Confirm");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        SaveOfflineManager.clearSharedPreference(getContext());
                        getActivity().finishAffinity();
                        startActivity(new Intent(getActivity(), LoginPage.class));
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
        //getAddress();//------getting address please check here

        uploadProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMultiplePermissions();
                showPictureDialog();
            }
        });
        return view;

    }


    public void getAddress() {
        String address = "";
        userAddress.setText(address);
    }


    public void updateData(String imageURL) {
        String name = usernameEditText.getText().toString();
        String address = userAddress.getText().toString();
        String phone = userphoneEditText.getText().toString();
        String custid = SaveOfflineManager.getUserId(getContext());
        Log.e("tag", "" + name + " " + address + " " + phone + " " + custid);
        ApiInterface apiInterface = Client.getClient().create(ApiInterface.class);
        Call<YesNoResponse> updateUserInfo = apiInterface.updateCutUserDetails(name, phone, address, custid,imageURL);
        updateUserInfo.enqueue(new Callback<YesNoResponse>() {
            @Override
            public void onResponse(Call<YesNoResponse> call, Response<YesNoResponse> response) {
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Changes will take sometime to reflect.", Snackbar.LENGTH_LONG).show();
                saveOffline(name, address, phone);
            }

            @Override
            public void onFailure(Call<YesNoResponse> call, Throwable t) {
                Log.e("tag", "" + t);
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Data Update Failed.Try Again", Snackbar.LENGTH_LONG).show();
            }
        });
    }
    private void saveOffline(String name, String adress,String phone) {
        SaveOfflineManager.setUserName(getContext(),name);
        SaveOfflineManager.setUserAddress(getContext(),adress);
        SaveOfflineManager.setUserPhoneNumber(getContext(),phone);
    }

    private void requestMultiplePermissions() {
        Dexter.withContext(getContext())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check(
                );
    }

    private void openSettingsDialog() {
        AlertDialog.Builder permissionDialog = new AlertDialog.Builder(getActivity());
        permissionDialog.setTitle("Permission required");
        permissionDialog.setMessage("Profile image upload required permission of your camera and external storage");

        permissionDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestMultiplePermissions();
            }
        });
        permissionDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getActivity(), UserFragment.class));
            }
        }).show();
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY && resultCode == RESULT_OK) {
            imageUri = data.getData();
            startCrop(imageUri);

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageUri = getImageUri(getContext(), thumbnail);
            startCrop(imageUri);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri uri = UCrop.getOutput(data);
            profileImg.setImageURI(uri);
        }
    }

    private void FirebaseUpload() {

        if (picture != null) {
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(true);
            progressDialog.setTitle("Please Wait...");
            progressDialog.show();
            StorageReference reference = storageRef.child("CustomerUser/" + firebaseAuth.getCurrentUser().getUid());
            reference.putFile(picture).addOnSuccessListener(taskSnapshot -> {
                progressDialog.dismiss();
                reference.getDownloadUrl().addOnSuccessListener(uri -> {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(uri)
                            .build();
                    firebaseAuth.getCurrentUser().updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        setProfileImage();
                                    }
                                }
                            });
                    updateData(uri.toString());
                });
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                updateData(null);
            });
        }else
        {
            updateData(null);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void setProfileImage() {
        Uri imageUri = firebaseAuth.getCurrentUser().getPhotoUrl();
        if (imageUri != null) {
            Picasso.get()
                    .load(imageUri.toString())
                    .resize(500, 500)
                    .centerCrop()
                    .into(profileImg);
        } else {
            profileImg.setImageResource(R.drawable.profile001);
        }
    }

    Uri picture, imageUri;

    private void startCrop(@NonNull Uri uri) {
        String des = "pic" + System.currentTimeMillis() + ".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getActivity().getCacheDir(), des)));
        uCrop.withAspectRatio(1, 1);
        uCrop.withMaxResultSize(450, 450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(getActivity(), this);
        picture = Uri.fromFile(new File(getActivity().getCacheDir(), des));
    }

    private UCrop.Options getCropOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        options.setCompressionQuality(50);
        options.setToolbarTitle("Crop Your Image");
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        return options;
    }

}
