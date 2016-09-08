package com.robusta.task.photogallery.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.View;

import com.robusta.task.photogallery.custom.Span;

/**
 * Created by shanan on 08/09/2016.
 */
public class Utility {

    // Checking Network availability
    public static boolean getNetworkState(Context ctx) {

        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    public static Span getRecyclerViewSpan(Activity activity) {
        /**
         * Get screen dimensions
         */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int spanCount = displayMetrics.widthPixels < displayMetrics.heightPixels ? 2 : 3;

        int spanWidth = displayMetrics.widthPixels / spanCount;

        return new Span(spanCount, spanWidth);
    }

    public static int getRecyclerViewItemHeight(Activity activity){
        /**
         * Get screen dimensions
         */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int spanCount = 2;

        int spanHeight = displayMetrics.heightPixels / spanCount;

        return spanHeight;
    }

    public static int getRecyclerViewItemWidth(Activity activity){
        /**
         * Get screen dimensions
         */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int spanCount = displayMetrics.widthPixels < displayMetrics.heightPixels ? 2 : 3;

        int spanWidth = displayMetrics.widthPixels / spanCount;

        return spanWidth;
    }

    public static void showSnackBar(View view, int resId) {
        Snackbar.make(view, resId, Snackbar.LENGTH_LONG).show();
    }

}
