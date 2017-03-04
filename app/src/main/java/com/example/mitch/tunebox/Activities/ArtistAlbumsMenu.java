package com.example.mitch.tunebox.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.mitch.tunebox.CustomViews.AlbumImageViewAdapter;
import com.example.mitch.tunebox.CustomViews.ArtistAdapter;
import com.example.mitch.tunebox.Model.Album;
import com.example.mitch.tunebox.Model.AlbumArray;
import com.example.mitch.tunebox.Model.AllMusic;
import com.example.mitch.tunebox.Model.AlbumArt;
import com.example.mitch.tunebox.CustomViews.ArtistAlbumsAdapter;
import com.example.mitch.tunebox.Model.Artist;
import com.example.mitch.tunebox.Model.MusicService;
import com.example.mitch.tunebox.Model.Singleton;
import com.example.mitch.tunebox.R;
import com.example.mitch.tunebox.Model.Song;
import com.example.mitch.tunebox.Model.SongArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Mitch on 9/29/16.
 */
public class ArtistAlbumsMenu extends AppCompatActivity {
    private AlbumArray artistAlbums;
    private SongArray albumSongs;
    private SongArray songList;
    private ListView lstViewArtistAlbums;
    private Singleton singleton;
    public MusicService musicSrv;
    public boolean paused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_albums_menu);

        artistAlbums = new AlbumArray();
        songList = new SongArray();
        albumSongs = new SongArray();

        Bundle b = getIntent().getExtras();

        artistAlbums = b.getParcelable("artistAlbums");
        lstViewArtistAlbums = (ListView) findViewById(R.id.lstArtistAlbums);

        getSongs();
        setAlbumImages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            paused = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        musicSrv = null;
        super.onDestroy();
    }

    private void getSongs() {
        singleton = Singleton.getInstance();
        AllMusic allMusic = singleton.getAllMusic();
        songList = new SongArray();
        songList = allMusic.getSongList();
    }

    public void setAlbumImages(){
        //get artwork for albums
        AlbumArt albumArt = new AlbumArt(this);
        Collections.sort(artistAlbums, new Comparator<Album>() { //sorts songList
            @Override
            public int compare(Album a, Album b) {
                return a.getYear().compareTo(b.getYear());    //sort artists
            }
        });
        artistAlbums = albumArt.getArtistAlbums(artistAlbums);
        AlbumImageViewAdapter adapter = new AlbumImageViewAdapter(this, artistAlbums);
        lstViewArtistAlbums.setAdapter(adapter);
    }


    public void artistAlbumPicked(View view) {
        int currView = Integer.parseInt(view.getTag().toString());              // get current album
        String currAlbum = artistAlbums.get(currView).getAlbum().toString();
        albumSongs.clear();

        for(int i= 0; i < (songList.size()-1); i++){
            if(songList.get(i).album.equals(currAlbum)){                        //get songs from current album;
                Song s = new Song(songList.get(i).getID(), songList.get(i).getTitle(),
                        songList.get(i).getTrackNo(), songList.get(i).getArtist(), songList.get(i).getAlbum(),
                        songList.get(i).getArtistID(), songList.get(i).getAlbumID(),
                        songList.get(i).getDuration(), songList.get(i).getYear());
                albumSongs.add(s);

            }
        }
        Bundle b = new Bundle();
        b.putParcelable("albumSongs", albumSongs);                          //pass songs using bundle and launch AlbumSongsMenu
        Intent I = new Intent(this, AlbumsSongsMenu.class);
        I.putExtras(b);
        startActivity(I);
    }


}
