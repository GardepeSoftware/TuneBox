package com.example.mitch.tunebox.Model.ADT;

/**
 * Created by Mitch on 9/19/16.
 */
public class Album {
    //class for creating ArtistArray elements

    private String album;

    public Album(String currAlbum) {
        if (currAlbum.isEmpty()) {
            album = "Unknown";
        } else {
            album = currAlbum;
        }
    }

    public String getAlbum(){return album;}
}