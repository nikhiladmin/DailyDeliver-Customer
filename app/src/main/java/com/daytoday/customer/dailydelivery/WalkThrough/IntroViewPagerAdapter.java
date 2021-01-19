package com.daytoday.customer.dailydelivery.WalkThrough;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.daytoday.customer.dailydelivery.R;

import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {
    Context context;
    List<ScreenItem> Data;

    public IntroViewPagerAdapter(Context context, List<ScreenItem> data) {
        this.context = context;
        Data = data;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen =  inflater.inflate(R.layout.layout_walk_through,null);

        /* Get All the ID */
        ImageView walk_through_img = layoutScreen.findViewById(R.id.walk_through_image);
        TextView walk_through_text = layoutScreen.findViewById(R.id.walk_through_text);
        TextView title = layoutScreen.findViewById(R.id.title);
        /* Set All The ID */
        walk_through_img.setImageResource(Data.get(position).getScreenImg());
        walk_through_text.setText(Data.get(position).getDescription());
        title.setText(Data.get(position).getTitle());
        DisplayMetrics displayMetrics = container.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        double displaydp = Math.sqrt(dpHeight*dpHeight + dpWidth * dpWidth);
        walk_through_text.setTextSize((float) (displaydp *0.025));
//        walk_through_img.getLayoutParams().height = (int)(dpHeight * 1.0);
//        walk_through_img.getLayoutParams().width = (int)(dpWidth * 1.3);
        container.addView(layoutScreen);

        return layoutScreen;
    }

    @Override
    public int getCount() {
        return Data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
