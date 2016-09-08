package com.robusta.task.photogallery.utils;

import android.content.Context;
import android.content.Intent;

import com.robusta.task.photogallery.gallery.ImageViewerActivity;

import java.util.List;

/**
 * Created by shanan on 08/09/2016.
 */
public class Navigator {

    public static void navigateToImageViewer(Context context, List<String> images, int selectedImage) {
        if (context != null) {
            Intent intent = ImageViewerActivity.buildIntent(context, images, selectedImage);
            context.startActivity(intent);
        }
    }
}
