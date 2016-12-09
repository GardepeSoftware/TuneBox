package com.example.mitch.tunebox.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mitch.tunebox.Model.ADT.Song;
import com.example.mitch.tunebox.R;

import java.util.ArrayList;

/**
 * Created by Mitch on 9/29/16.
 */
public class AlbumSongsAdapter extends BaseAdapter {
    private ArrayList<Song> songs;
    private LayoutInflater songInf;

    public AlbumSongsAdapter(Context c, ArrayList<Song> theSongs){
        songs=theSongs;
        songInf=LayoutInflater.from(c);                     //sets up adapter using songList
    }

    @Override
    public int getCount() {
        return songs.size();
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
        LinearLayout SongLay = (LinearLayout)songInf.inflate
                (R.layout.album_songs, parent, false);
        TextView songView = (TextView)SongLay.findViewById(R.id.txvAlbumSong);
        //get song using position
        Song currSong = songs.get(position);                      //creates individual textview elements
        songView.setText(currSong.getTitle());
        //set position as tag
        SongLay.setTag(position);
        return SongLay;
    }
}