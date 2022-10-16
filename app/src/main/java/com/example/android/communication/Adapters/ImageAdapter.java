package com.example.android.communication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


//IMAGE SLIDER ADAPTER
public class ImageAdapter extends PagerAdapter {

    //Variables
    private Context mContext;
    private int[] mImageIds;

    public ImageAdapter(Context context, int[] sources) {
        mContext = context;
        mImageIds = sources;
    }

    //Get the amount of images
    @Override
    public int getCount() {
        return mImageIds.length;
    }

    //Return object
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    //Set the images
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageResource(mImageIds[position]);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
