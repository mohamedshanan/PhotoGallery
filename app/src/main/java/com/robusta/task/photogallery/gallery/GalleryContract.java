package com.robusta.task.photogallery.gallery;

import java.util.List;

/**
 * Created by shanan on 08/09/2016.
 */
public interface GalleryContract {
    interface View {
        void showImages(boolean clearing, List<String> imagesList);

        void showLoadMoreProgress();

        void dismissLoadMoreProgress();

        void clearImages();

        void showSnackBar(int resId);

        void showTryAgainLayout(int resId);

        void hideTryAgainLayout();

        void showSwipeContainerIndicator(boolean refreshing);
    }

    interface ActionsListener {
        void loadImages(boolean isFirstLoad);

        //void refreshImages();
    }
}