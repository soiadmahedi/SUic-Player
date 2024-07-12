package com.soiadmahedi.suicTh;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

public class VideoInfoUtils {
    public Bitmap bitmap = null;
    public final Context myContext;
    public String path;

 
    /*  Soiad Mahedi - From Bangladesh  */

    public VideoInfoUtils(Context context, String str) {
        this.myContext = context;
        this.path = str;
    }

    public String bites() {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this.path);
        return mediaMetadataRetriever.extractMetadata(20);
    }

    public String duration() {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this.path);
        return mediaMetadataRetriever.extractMetadata(9);
    }

    public String frames() {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this.path);
        return mediaMetadataRetriever.extractMetadata(32);
    }

    public String height() {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this.path);
        return mediaMetadataRetriever.extractMetadata(19);
    }

    public String width() {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this.path);
        return mediaMetadataRetriever.extractMetadata(18);
    }

    public Context getContext() {
        return this.myContext;
    }
}