package com.example.mitch.tunebox.Model.ADT;

/**
 * Created by Mitch on 9/19/16.
 */
public class Album {
    //class for creating ArtistArray elements

    private String album;
    private Long albumID;

    public Album(String userAlbum, Long userAlbumID) {
        if (userAlbum.isEmpty()) {
            album = "Unknown";
        } else {
            album = userAlbum;
        }

        albumID = userAlbumID;
    }

    public String getAlbum(){return album;}

    public Long getAlbumID(){return albumID;}
}