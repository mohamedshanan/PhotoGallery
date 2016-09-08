package com.robusta.task.photogallery.gallery;

import android.content.Context;

import com.robusta.task.photogallery.R;
import com.robusta.task.photogallery.utils.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by shanan on 08/09/2016.
 */
public class GalleryPresenter implements GalleryContract.ActionsListener {
    //NUMBER OF IMAGES
    private static int PAGE_ITEMS = 8;
    private static final int MAX_DIMEN = 800;
    private static final int MIN_DIMEN = 200;

    Context mContext;
    GalleryContract.View view;

    public GalleryPresenter(Context context, GalleryContract.View view){
        this.mContext = context;
        this.view = view;
    }

    @Override
    public void loadImages(boolean isFirstLoad) {

        if (!Utility.getNetworkState(mContext)) {
            return;
        }

        // Get images with different dimensions based on generated random integers.
        List<String> urls = new ArrayList<>();

        for(int i = 0; i < PAGE_ITEMS; i++){
            urls.add("http://lorempixel.com/"+generateRandom()
                    +"/"+generateRandom()+"/");
        }

        if (urls.size() == 0) {
            view.showTryAgainLayout(R.string.no_data);
            view.clearImages();
            return;
        }

        view.hideTryAgainLayout();

        view.showImages(isFirstLoad, urls);

    }

    private int generateRandom(){
        // Getting random number between MIN_DIMEN and MAX_DIMEN to be the dimens
        // of the image
        Random r = new Random();
        return r.nextInt(MAX_DIMEN - MIN_DIMEN) + MIN_DIMEN;
    }
}
