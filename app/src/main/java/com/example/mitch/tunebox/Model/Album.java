package com.example.mitch.tunebox.Model;

import android.graphics.Bitmap;

/**
 * Created by Mitch on 9/19/16.
 */
public class Album {
    //class for creating ArtistArray elements

    private String album;
    private Long albumID;
    private Bitmap artwork;
    private String year;

    public Album(String userAlbum, Long userAlbumID, String mYear) {
        if (userAlbum.isEmpty()) {
            album = "Unknown";
        } else {
            album = userAlbum;
        }
        albumID = userAlbumID;
        year = mYear;
    }

    public void setArtwork(Bitmap art) {
        artwork = art;
    }

    public String getAlbum(){return album;}

    public Long getAlbumID(){return albumID;}

    public Bitmap getArtwork() {return artwork;}

    public String getYear(){return year;}
}