package com.robusta.task.photogallery.gallery;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robusta.task.photogallery.custom.EndlessRecyclerViewScrollListener;
import com.robusta.task.photogallery.R;
import com.robusta.task.photogallery.custom.Span;
import com.robusta.task.photogallery.utils.Utility;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A fragment containing a RecyclerView.
 */
public class MainActivityFragment extends Fragment implements GalleryContract.View{

    private static final String KEY_RV_STATE = "rvState";
    private static final String KEY_IMAGES_LIST = "newsItemsList";


    // Binding views with butterknife library
    @BindView(R.id.recycler_view) RecyclerView rvImages;
    @BindView(R.id.tvMessage) TextView tvMessage;
    @BindView(R.id.tryAgainLayout) ViewGroup tryAgainLayout;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;

    private GalleryRVAdapter mRVAdapter;

    private GalleryContract.ActionsListener actionsListener;

    private Parcelable mListState;
    private StaggeredGridLayoutManager mLayoutManager;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionsListener = new GalleryPresenter(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, rootView);

        // Setting colors and Implementing OnRefresh for the SwipeRefreshLayout

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwipeRefreshLayout.setOnRefreshListener(() -> actionsListener.loadImages(true));

        // Restore image list state if state is saved or Load remote images otherwise
        if (savedInstanceState == null) {

            initRV(new ArrayList<>());
            /** Load remote images */
            actionsListener.loadImages(true);
        } else {
            /** Restore images*/
            List<String> images = Parcels.unwrap(savedInstanceState.getParcelable(KEY_IMAGES_LIST));
            initRV(images);

            /** Retrieve list state and list/item positions */
            mListState = savedInstanceState.getParcelable(KEY_RV_STATE);
        }
        return rootView;
    }

    @Override public void showSwipeContainerIndicator(boolean refreshing) {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(refreshing));
    }

    @Override
    public void showImages(boolean clearing, List<String> imagessList) {
        if (clearing) {
            mRVAdapter.setImages(imagessList);
            mLayoutManager.scrollToPosition(0);
        } else {
            mRVAdapter.addImages(imagessList);
        }
    }

    @Override
    public void showLoadMoreProgress() {
        mRVAdapter.showLoadMoreProgress();
    }

    @Override
    public void dismissLoadMoreProgress() {
        mRVAdapter.dismissLoadMoreProgress();
    }

    @Override
    public void clearImages() {
        mRVAdapter.clear();
    }

    @Override
    public void showSnackBar(int resId) {
        Utility.showSnackBar(rvImages, resId);
    }

    @Override
    public void showTryAgainLayout(int resId) {
        tryAgainLayout.setVisibility(View.VISIBLE);
        tvMessage.setText(resId);
    }

    @Override
    public void hideTryAgainLayout() {
        tryAgainLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListState != null) {
            //Restore RecyclerView state - position
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    @OnClick(R.id.tryAgainLayout) public void onClickTryAgain() {
        actionsListener.loadImages(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(KEY_RV_STATE, mListState);

        outState.putParcelable(KEY_IMAGES_LIST, Parcels.wrap(mRVAdapter.getImages()));
    }

    private void initRV(List<String> imagesUrls) {
        rvImages.setHasFixedSize(true);

        Span span = Utility.getRecyclerViewSpan(getActivity());
        mLayoutManager = new StaggeredGridLayoutManager(
                span.count, StaggeredGridLayoutManager.VERTICAL);

        // Create adapter
        mRVAdapter = new GalleryRVAdapter(imagesUrls, getActivity());

        rvImages.setLayoutManager(mLayoutManager);

        rvImages.setAdapter(mRVAdapter);

        rvImages.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore() {
                actionsListener.loadImages(false);
            }
        });
    }
}
