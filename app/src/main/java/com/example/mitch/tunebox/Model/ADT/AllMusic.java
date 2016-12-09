package com.example.mitch.tunebox.Model.ADT;

/**
 * Created by Mitch on 12/8/16.
 */

public class AllMusic {
    private SongArray songList;
    private ArtistArray artistList;
    private AlbumArray albumList;


    public void setSongList(SongArray userSongs) {
        this.songList = userSongs;
    }

    public void setArtistList(ArtistArray userArtists) {
        artistList = userArtists;
    }

    public void setAlbumList(AlbumArray userAlbums) {
        albumList = userAlbums;
    }

    public SongArray getSongList() {
       return songList;
    }

    public ArtistArray getArtistList() {
        return artistList;
    }

    public AlbumArray getAlbumList() {
        return albumList;
    }
}
