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
 * Created by Mitch on 9/19/16.
 */
public class AlbumAdapter extends BaseAdapter {
    private ArrayList<Album> albums;
    private LayoutInflater albumInf;

    public AlbumAdapter(Context c, ArrayList<Album> theAlbums){
        albums=theAlbums;                   //creates an adapter for Albums
        albumInf=LayoutInflater.from(c);
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

    //used for targeting individual elements in the ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout AlbumLay = (LinearLayout)albumInf.inflate
                (R.layout.album, parent, false);
        TextView AlbumView = (TextView)AlbumLay.findViewById(R.id.txvAlbum);
        //get song using position
        Album currAlbum = albums.get(position);
        AlbumView.setText(currAlbum.getAlbum());            //creates TextViews for elements
        //set position as tag
        AlbumLay.setTag(position);
        return AlbumLay;
    }
}