package com.example.mitch.tunebox.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mitch.tunebox.Model.ADT.Album;
import com.example.mitch.tunebox.R;

import java.util.ArrayList;

/**
 * Created by Mitch on 9/29/16.
 */
public class ArtistAlbumsAdapter extends RelativeLayout {
    private ImageView img1;
    private ImageView img2;
    private Bitmap bit1;
    private Bitmap bit2;
    private int tag1;
    private int tag2;


    public ArtistAlbumsAdapter(Context context) {
        super(context);
    }

    public void setBitmap1(Bitmap b1, int t1) {
        bit1 = b1;
        tag1 = t1;
    }

    public void setBitmap2(Bitmap b2, int t2) {
        bit2 = b2;
        tag2 = t2;
    }


    public void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.album_image_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // Sets the images for the previous and next buttons. Uses
        // built-in images so you don't need to add images, but in
        // a real application your images should be in the
        // application package so they are always available.
        img1 = (ImageView) this.findViewById(R.id.albumImage1);
        img1.setImageBitmap(bit1);
        img2 = (ImageView)this.findViewById(R.id.albumImage2);
        img2.setImageBitmap(bit2);
    }

    public void setImageViews() {
        img1 = (ImageView) this.findViewById(R.id.albumImage1);
        img1.setImageBitmap(bit1);
        img1.setTag(tag1);
        img2 = (ImageView)this.findViewById(R.id.albumImage2);
        img2.setImageBitmap(bit2);
        img2.setTag(tag2);
    }

}