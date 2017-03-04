package com.example.mitch.tunebox.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.mitch.tunebox.CustomViews.SongAdapter;
import com.example.mitch.tunebox.Model.Album;
import com.example.mitch.tunebox.Model.AllMusic;
import com.example.mitch.tunebox.Model.Artist;
import com.example.mitch.tunebox.Model.MusicService;
import com.example.mitch.tunebox.Model.Singleton;
import com.example.mitch.tunebox.Model.Song;
import com.example.mitch.tunebox.Model.SongArray;
import com.example.mitch.tunebox.R;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Mitch on 2/25/17.
 */

public class SongMenu extends AppCompatActivity {
    private SongArray songList;
    private Singleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_menu);

        getSongs();
        fillListView();
        setButtons();
    }

    private void getSongs() {
        singleton = Singleton.getInstance();
        AllMusic allMusic = singleton.getAllMusic();
        songList = allMusic.getSongList();
        Collections.sort(songList, new Comparator<Song>() {
            @Override
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
    }

    private void fillListView() {
        ListView songListView = (ListView) findViewById(R.id.lstSongs);
        SongAdapter adapter = new SongAdapter(this, songList);
        songListView.setAdapter(adapter);
    }

    private void setButtons() {
        Button btnSongs = (Button) findViewById(R.id.btnSongs_Songs);
        Button btnAlbums = (Button) findViewById(R.id.btnSongs_Albums);
        Button btnArtists = (Button) findViewById(R.id.btnSongs_Artists);

        btnSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //already in songs, do nothing
            }
        });

        btnAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlbumMenu.class);
                startActivity(intent);
            }
        });

        btnArtists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), ArtistMenu.class);
                startActivity(intent1);
            }
        });
    }

    public void songPicked(View view) {
        setSong(view);
    }

    private void setSong(View view) {
        MusicService musicSrv = singleton.getService();
        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        musicSrv.setList(songList);
        musicSrv.playSong();
        startNextActivity(view);
    }

    private void startNextActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), PlayScreen.class);
        int index = Integer.parseInt(view.getTag().toString());
        Song currentSong = songList.get(index);
        Intent I = new Intent(this, PlayScreen.class);
        I.putExtra("albumID", currentSong.albumID);
        I.putExtra("albumName", currentSong.getAlbum());
        I.putExtra("artistName", currentSong.getArtist());
        I.putExtra("currSong", currentSong.getTitle());
        I.putExtra("playList", (Parcelable)songList);
        I.putExtra("songIndex", index);
        startActivity(intent);
    }
}

