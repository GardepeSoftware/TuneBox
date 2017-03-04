package com.example.mitch.tunebox.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.mitch.tunebox.Model.Album;
import com.example.mitch.tunebox.CustomViews.AlbumAdapter;
import com.example.mitch.tunebox.Model.AlbumArray;
import com.example.mitch.tunebox.Model.MusicService;
import com.example.mitch.tunebox.R;
import com.example.mitch.tunebox.Model.Song;
import com.example.mitch.tunebox.Model.SongArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Mitch on 9/19/16.
 */
public class AlbumMenu extends AppCompatActivity {
    private ArrayList<Album> artistAlbums;
    private SongArray albumSongs;
    private AlbumArray albumList;
    private SongArray songList;
    private ListView albumsView;
    public MusicService musicSrv;
    public boolean paused = false;
    public boolean playbackPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_menu);

        albumList = new AlbumArray();
        songList = new SongArray();
        albumSongs = new SongArray();

        Bundle b = getIntent().getExtras();

        albumList = b.getParcelable("albumList");               //gets parcelable arrays from SongMenu
        songList = b.getParcelable("songList");

        albumsView = (ListView) findViewById(R.id.lstAlbums);     //links to ListView on xml


        Collections.sort(albumList, new Comparator<Album>() { //sorts songList
            @Override
            public int compare(Album a, Album b) {
                return a.getAlbum().compareTo(b.getAlbum());    //sorts array
            }
        });

        AlbumAdapter AlbumAdt = new AlbumAdapter(this, albumList);
        albumsView.setAdapter(AlbumAdt);                   //uses adapter to map albums to ListView


        Button BSong = (Button)findViewById(R.id.btnAlbumSongs);
        final View.OnClickListener OSongs = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ISong = new Intent(v.getContext(), SongMenu.class);          //OnClickListener for SongMenu
                startActivity(ISong);
            }
        };
        BSong.setOnClickListener(OSongs);

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


    public void albumPicked(View view) {
        int currView = Integer.parseInt(view.getTag().toString());
        String currAlbum = albumList.get(currView).getAlbum().toString();       //gets current view's album name
        albumSongs.clear();

        for(int i= 0; i < (songList.size()-1); i++){
            if(songList.get(i).album.equals(currAlbum)){                    //creates a list of songs for current album
                Song s = new Song(songList.get(i).getID(), songList.get(i).getTitle(),
                        songList.get(i).getTrackNo(), songList.get(i).getArtist(), songList.get(i).getAlbum(),
                        songList.get(i).getArtistID(), songList.get(i).getAlbumID(),
                        songList.get(i).getDuration(), songList.get(i).getYear());
                albumSongs.add(s);

            }
        }
        Bundle b = new Bundle();
        b.putParcelable("albumSongs", albumSongs);                          //passes albumSongs to AlbumSongsMenu
        Intent I = new Intent(this, AlbumsSongsMenu.class);
        I.putExtras(b);
        startActivity(I);
    }
}

