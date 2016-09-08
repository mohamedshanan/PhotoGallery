package com.robusta.task.photogallery.gallery;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.robusta.task.photogallery.R;

import java.io.File;

/**
 * Created by shanan on 08/09/16.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize Iconify
        Iconify.with(new MaterialModule());

        // initialize ImageLoader
        initImageLoader();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initImageLoader() {
        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(this).memoryCacheExtraOptions(480,
                        800) // default = device screen dimensions
                        .diskCacheExtraOptions(480, 800, null)
                        .threadPoolSize(3) // default
                        .threadPriority(Thread.NORM_PRIORITY - 2) // default
                        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                        .denyCacheImageMultipleSizesInMemory()
                        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                        .memoryCacheSize(2 * 1024 * 1024)
                        .memoryCacheSizePercentage(13) // default
                        .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                        .diskCacheSize(50 * 1024 * 1024)
                        .diskCacheFileCount(100)
                        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                        .imageDownloader(new BaseImageDownloader(this)) // default
                        .imageDecoder(new BaseImageDecoder(true)) // default
                        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                        .writeDebugLogs()
                        .build();
        ImageLoader.getInstance().init(config);
    }
}
