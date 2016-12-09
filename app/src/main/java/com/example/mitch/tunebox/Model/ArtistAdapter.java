package com.example.mitch.tunebox.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mitch.tunebox.Model.ADT.Artist;
import com.example.mitch.tunebox.R;

import java.util.ArrayList;

/**
 * Created by Mitch on 9/29/16.
 */
public class ArtistAdapter extends BaseAdapter {
    private ArrayList<Artist> artists;
    private LayoutInflater artistInf;

    public ArtistAdapter(Context c, ArrayList<Artist> theArtists){
        artists=theArtists;
        artistInf=LayoutInflater.from(c);           //creates adapter for artistList
    }

    @Override
    public int getCount() {
        return artists.size();
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
        LinearLayout ArtistLay = (LinearLayout)artistInf.inflate
                (R.layout.artist, parent, false);
        //get title and artist views
        TextView artistView = (TextView)ArtistLay.findViewById(R.id.txvArtist);
        //get song using position
        Artist currArtist = artists.get(position);                  //creates individual TextView elements
        //get title and artist strings
        artistView.setText(currArtist.getArtist());
        //set position as tag
        ArtistLay.setTag(position);
        return ArtistLay;
    }
}