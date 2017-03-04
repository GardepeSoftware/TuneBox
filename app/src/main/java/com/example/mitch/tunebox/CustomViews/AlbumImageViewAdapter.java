package com.example.mitch.tunebox.CustomViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mitch.tunebox.Model.Album;
import com.example.mitch.tunebox.R;

import java.util.ArrayList;

/**
 * Created by Mitch on 3/4/17.
 */

public class AlbumImageViewAdapter extends BaseAdapter {
    private ArrayList<Album> albums;
    private LayoutInflater albumInflator;

    public AlbumImageViewAdapter(Context c, ArrayList<Album> mAlbum) {
        albums = mAlbum;
        albumInflator = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return albums.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout albumLay = (LinearLayout)albumInflator.inflate
                (R.layout.album_image_view, parent, false);
        //get title and artist views
        ImageView albumImage = (ImageView)albumLay.findViewById(R.id.albumImage);
        TextView album = (TextView)albumLay.findViewById(R.id.txtViewAlbum);
        TextView year = (TextView)albumLay.findViewById(R.id.txtViewYear);
        Album currAlbum= albums.get(position);
        if(currAlbum.getYear() != null) {
            year.setText("(" + currAlbum.getYear() + ")");
        } else {
            year.setText("Unknown");
        }
        if(currAlbum.getAlbum() != null) {
            album.setText(currAlbum.getAlbum());
        } else {
            year.setText("Unknown");
        }
        albumImage.setImageBitmap(currAlbum.getArtwork());
        albumLay.setTag(position);
        return albumLay;
    }
}
