package com.example.mitch.tunebox.Model.ADT;

/**
 * Created by Mitch on 9/29/16.
 */
public class Artist{
    //creates items for ArtistArray
    public String artist;

    public Artist(String songArtist) {
        artist=songArtist;
    }
    public String getArtist(){return artist;}
}