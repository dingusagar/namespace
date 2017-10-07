package com.example.dingu.ctjourn;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ImageView;

import java.util.HashMap;

class ImageLoadingAsynTask extends AsyncTask<String, String, String> {

    private String resp;
    String videoURL;
    ImageView postImage;
    Bitmap bitmap = null;
    ImageLoadingAsynTask(String videoURL , ImageView postImage) {
        this.videoURL = videoURL;
        this.postImage = postImage;
    }


    @Override
    protected String doInBackground(String... params) {


        try {
            bitmap = retriveVideoFrameFromVideo(videoURL);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return resp;
    }


    @Override
    protected void onPostExecute(String result) {
        // execution of result of Long time consuming operation
        postImage.setImageBitmap(bitmap);
    }


    @Override
    protected void onPreExecute() {

    }


    @Override
    protected void onProgressUpdate(String... text) {


    }




    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
