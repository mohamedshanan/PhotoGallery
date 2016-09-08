package com.robusta.task.photogallery.gallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.robusta.task.photogallery.R;
import com.robusta.task.photogallery.custom.ExtendedViewPager;
import com.robusta.task.photogallery.custom.TouchImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ImageViewerActivity extends AppCompatActivity {
private static List<String> imgs;
private static int clickedPosition;
protected DisplayImageOptions options;

public static Intent buildIntent(Context context, List<String> imgURls, int SelectedImage){
    Intent intent = new Intent(context, ImageViewerActivity.class);
    imgs = new ArrayList<>();
    imgs = imgURls;
    clickedPosition = SelectedImage;
    return intent;
}

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_viewer);

    initializeImageViewerOptions();

    ExtendedViewPager mViewPager = (ExtendedViewPager)findViewById(R.id.view_pager);
    mViewPager.setAdapter(new TouchImageAdapter());
    // display the clicked item
    if(clickedPosition > 0) {
        mViewPager.setCurrentItem(clickedPosition);
    }

}

private void initializeImageViewerOptions() {
    IconDrawable onFailImage = new IconDrawable(this,
            MaterialIcons.md_do_not_disturb_alt)
            .colorRes(android.R.color.holo_red_light);

    IconDrawable emptyUriImage = new IconDrawable(this,
            MaterialIcons.md_broken_image)
            .colorRes(android.R.color.holo_red_light);

    options =  new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
            .bitmapConfig(Bitmap.Config.ARGB_8888)
            .showImageOnFail(onFailImage)
            .showImageForEmptyUri(emptyUriImage)
            .build();
}

class TouchImageAdapter extends PagerAdapter {
    private List<String> images = imgs;

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        TouchImageView img = new TouchImageView(container.getContext());

        ProgressBar progressIndicator = new ProgressBar(container.getContext(),
                null,
                android.R.attr.progressBarStyleHorizontal);

        progressIndicator.setVisibility(View.GONE);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(
                ImageViewerActivity.this, R.anim.fadein);
        img.startAnimation(fadeInAnimation);

        //displayImage(images.get(position), img);
        ImageLoader.getInstance().displayImage(images.get(position), img, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressIndicator.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressIndicator.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressIndicator.setVisibility(View.GONE);
            }
        });
        //
        container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        return img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
protected void displayImage(String url, ImageView imageView) {

}
}