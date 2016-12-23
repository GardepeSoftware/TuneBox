package com.example.mitch.tunebox.Model.ADT;

/**
 * Created by Mitch on 9/29/16.
 */
public class Song{
    public long id;
    public int trackNo;
    public String title;
    public String artist;
    public String album;
    public int artistID;
    public long albumID;

    public Song(long songID, String songTitle, int track, String songArtist, String songAlbum, int songArtistID, long songAlbumID) {
        id=songID;
        title=songTitle;
        trackNo = track;
        artist=songArtist;
        album=songAlbum;
        artistID=songArtistID;
        albumID = songAlbumID;
    }

    public long getID(){return id;}
    public String getTitle(){return title;}
    public int getTrackNo(){return trackNo;}
    public String getArtist(){return artist;}
    public int getArtistID(){return artistID;}
    public String getAlbum(){return album;}
    public long getAlbumID(){
        return albumID;
    }


}