package com.robusta.task.photogallery.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.robusta.task.photogallery.R;
import com.robusta.task.photogallery.utils.Navigator;
import com.robusta.task.photogallery.utils.Utility;

import java.net.URI;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by shanan on 08/09/16.
 */

public class GalleryRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static String TAG = GalleryRVAdapter.class.getSimpleName();

  // View type for image item
  private final int VIEW_ITEM = 0;
  // View type for progress item
  private final int VIEW_PROG = 1;

  // Universal Image loader options
  private DisplayImageOptions options;

  private List<String> urlList;

  private Context mContext;

  // Pass in the images list into the constructor
  public GalleryRVAdapter(List<String> urls, Context context) {

    mContext = context;
    urlList = urls;

    // Initialize the image loader options
    initializeImageViewerOptions();
  }

  // Usually involves inflating a layout from XML and returning the holder
  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    RecyclerView.ViewHolder viewHolder;
    if (viewType == VIEW_ITEM) {
      View view =
              LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);

      viewHolder = new ImageViewHolder(view);
    } else {
      View view =
              LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);

      viewHolder = new ProgressViewHolder(view);
    }
    return viewHolder;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

    if (viewHolder instanceof ImageViewHolder) {
      ImageViewHolder imageViewHolder = (ImageViewHolder) viewHolder;

      // Get the imgage url model based on position
      String imageUrl = urlList.get(position);

      // Set item views based on the data model
      ImageLoader.getInstance().displayImage(imageUrl, imageViewHolder.gridImageView, options, new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {
          imageViewHolder.gridImageView.setVisibility(View.GONE);
          imageViewHolder.imageLoadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
          imageViewHolder.imageLoadingBar.setVisibility(View.GONE);
          imageViewHolder.gridImageView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
          imageViewHolder.imageLoadingBar.setVisibility(View.GONE);
          imageViewHolder.gridImageView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
          imageViewHolder.imageLoadingBar.setVisibility(View.GONE);
          imageViewHolder.gridImageView.setVisibility(View.VISIBLE);
        }
      });

    } else {
      ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);
    }
  }

  @Override public int getItemCount() {
    return urlList.size();
  }

  @Override public int getItemViewType(int position) {
    return urlList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
  }

  public void showLoadMoreProgress() {
    //add progress item
    urlList.add(null);
    notifyItemInserted(urlList.size());
  }

  public void dismissLoadMoreProgress() {
    //remove progress item
    urlList.remove(urlList.size() - 1);
    notifyItemRemoved(urlList.size());
  }

  public void clear() {
    urlList.clear();
    notifyDataSetChanged();
  }

  public List<String> getImages() {
    return urlList;
  }

  // Set a list of items
  public void setImages(List<String> images) {
    urlList.clear();
    urlList.addAll(images);
    notifyDataSetChanged();
  }

  // Add a list of items
  public void addImages(List<String> images) {
    int positionStart = getItemCount();
    urlList.addAll(images);
    notifyItemRangeInserted(positionStart, urlList.size());
  }

  private void initializeImageViewerOptions() {

    IconDrawable onFailImage = new IconDrawable(mContext,
            MaterialIcons.md_do_not_disturb_alt)
              .colorRes(android.R.color.holo_red_light);

    IconDrawable emptyUriImage = new IconDrawable(mContext,
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

  public class ImageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.grid_img) ImageView gridImageView;
    @BindView(R.id.progressBar) ProgressBar imageLoadingBar;

    public ImageViewHolder(View itemView) {

      super(itemView);

      ButterKnife.bind(this, itemView);

    }

    // open clicked image on new Activity
    @OnClick(R.id.grid_img) public void onClick() {
      Log.d(GalleryRVAdapter.class.getSimpleName(), "" + urlList.get(getLayoutPosition()));
      // get the index of the clicked image
      int pos = getAdapterPosition();
      Navigator.navigateToImageViewer(mContext, urlList, pos);
    }

  }

  public static class ProgressViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public ProgressViewHolder(View v) {
      super(v);
      progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
    }
  }

}