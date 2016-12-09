package com.example.mitch.tunebox.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mitch.tunebox.Model.ADT.Album;
import com.example.mitch.tunebox.R;

import java.util.ArrayList;

/**
 * Created by Mitch on 9/29/16.
 */
public class ArtistAlbumsAdapter extends BaseAdapter {
    private ArrayList<Album> albums;
    private LayoutInflater albumInf;

    public ArtistAlbumsAdapter(Context c, ArrayList<Album> theAlbums){
        albums=theAlbums;
        albumInf=LayoutInflater.from(c);                    //creates adapter from albumList
    }

    @Override
    public int getCount() {
        return albums.size();
    }

    @Override
    public Object getItem(int arg0) {
        //TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        //TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout AlbumLay = (LinearLayout)albumInf.inflate
                (R.layout.album, parent, false);
        TextView albumView = (TextView)AlbumLay.findViewById(R.id.txvAlbum);            //creates individual TextViews
        //get song using position                                                       //using albumList
        Album currAlbum = albums.get(position);
        albumView.setText(currAlbum.getAlbum());
        //set position as tag
        AlbumLay.setTag(position);
        return AlbumLay;
    }
}