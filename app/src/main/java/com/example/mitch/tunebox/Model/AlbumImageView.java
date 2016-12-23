package com.example.mitch.tunebox.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mitch.tunebox.Model.ADT.Album;
import com.example.mitch.tunebox.R;

import java.util.ArrayList;

/**
 * Created by Mitch on 9/19/16.
 */
//replaced this class with ArtistAlbumsAdapter
public class AlbumImageView extends View {
    private Bitmap album1;
    private Bitmap album2;
    private LayoutInflater albumInf;

    public AlbumImageView(Context context, Bitmap a1, Bitmap a2) {
        super(context);
        this.album1 = a1;
        this.album2 = a2;
        albumInf = LayoutInflater.from(context);
    }


    //
    public View createView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout AlbumLay = (LinearLayout)albumInf.inflate
                (R.layout.album_image_view, parent, false);

        if(album1 != null) {
            ImageView image1 = (ImageView) AlbumLay.findViewById(R.id.albumImage1);
            image1.setImageBitmap(album1);
        }

        if(album2 != null) {
            ImageView image2 = (ImageView) AlbumLay.findViewById(R.id.albumImage2);
            image2.setImageBitmap(album2);
        }

        //set position as tag
        AlbumLay.setTag(position);
        return AlbumLay;
    }
}
