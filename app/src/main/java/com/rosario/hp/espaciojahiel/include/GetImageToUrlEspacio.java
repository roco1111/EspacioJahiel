package com.rosario.hp.espaciojahiel.include;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;



public class GetImageToUrlEspacio extends AsyncTask<Bitmap, Bitmap, Bitmap> {



    @Override
    protected void onPreExecute() {
    }


    @Override
    protected Bitmap doInBackground(Bitmap... params) {


        return params[0];
    }

    protected void onPostExecute(Bitmap result, ImageView imageView) {
        imageView.setImageBitmap(result);
    }
}