package com.example.brom.listviewjsonapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class GameDetails extends AppCompatActivity {
    protected ImageView bmImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        Intent intent = getIntent();
        String gameName = intent.getStringExtra("Name");
        String gameSeize = intent.getStringExtra("Height");
        String gameDesc = intent.getStringExtra("Desc");
        String imageUrl = intent.getStringExtra("Image");

        TextView textView1 = (TextView) findViewById(R.id.textview1);
        TextView textView2 = (TextView) findViewById(R.id.textview2);
        TextView textView3 = (TextView) findViewById(R.id.textview3);
        textView1.setText(gameName);
        textView2.setText(gameSeize);
        textView3.setText(gameDesc);

        new DownloadImageTask((ImageView) findViewById(R.id.mountain_image)).execute(imageUrl);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        public DownloadImageTask(ImageView bmImageIn) {
            bmImage = bmImageIn;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
