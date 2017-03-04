package com.example.mitch.tunebox.Model;

/**
 * Created by Mitch on 3/4/17.
 */

public class NowPlaying {
    private SongArray playlist;
    private int songIndex;
    private long albumID;
    private String artist;
    private String album;
    private String trackTitle;

    public NowPlaying(SongArray playist, int songIndex, long albumID, String artist, String album, String trackTitle) {
        this.playlist = playist;
        this.songIndex = songIndex;
        this.albumID = albumID;
        this.artist = artist;
        this.album = album;
        this.trackTitle = trackTitle;
    }

    public SongArray getPlaylist() {
        return playlist;
    }

    public int getSongIndex() {
        return songIndex;
    }

    public long getAlbumID() {
        return albumID;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getTrackTitle() {
        return trackTitle;
    }

}
