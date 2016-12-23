package com.example.mitch.tunebox.View;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mitch.tunebox.Controller.ArtistsController;
import com.example.mitch.tunebox.Controller.SongsController;
import com.example.mitch.tunebox.Model.ADT.AllMusic;
import com.example.mitch.tunebox.Model.Singleton;
import com.example.mitch.tunebox.Model.MusicService;
import com.example.mitch.tunebox.R;
import com.example.mitch.tunebox.Model.SQLiteHelper;
import com.example.mitch.tunebox.Model.ADT.Song;
import com.example.mitch.tunebox.Model.SongAdapter;
import com.example.mitch.tunebox.Model.ADT.SongArray;

import java.util.Collections;
import java.util.Comparator;

public class SongMenu extends Activity {
    private AllMusic music;
    private SongArray songList;
    private ListView songView;
    private String shuffleStatus = " false";
    private boolean musicBound = false;
    public MusicService musicSrv;
    public boolean paused = false;
    public boolean playbackPaused = false;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL = 0;
    public Singleton singleton;
    SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_menu);

        //pull music from memory and fill ListView
        SongsController controller = new SongsController();
        songList = controller.getSongs();
        fillList();

        setButtons();

        //WHY IS THIS HERE AND WHAT WAS I DOING????
        dbHelper = new SQLiteHelper(this);
        getShuffle();                                           //get shuffle status
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    //set button listeners
    public void setButtons() {
        Button BArtist = (Button) findViewById(R.id.btnArtist);
        final View.OnClickListener OArtist = new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                     //launches artist menu
                Intent IArtist = new Intent(v.getContext(), ArtistMenu.class);
                startActivity(IArtist);
            }
        };
        BArtist.setOnClickListener(OArtist);


        Button BSong = (Button) findViewById(R.id.btnSongs);                         //launches song menu
        final View.OnClickListener OSong = new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                       //set application class variables
                Intent IAlbum = new Intent(v.getContext(), AlbumMenu.class);
                startActivity(IAlbum);
            }
        };
        BSong.setOnClickListener(OSong);

        Button BAlbum = (Button) findViewById(R.id.btnAlbums);                       //launches album menu
        final View.OnClickListener OAlbums = new View.OnClickListener() {                   //onClickEvent for album button
            @Override
            public void onClick(View v) {                                     //set application class variables
                Intent IAlbum = new Intent(v.getContext(), AlbumMenu.class);
                startActivity(IAlbum);
            }
        };
        BAlbum.setOnClickListener(OAlbums);
    }

    private void fillList() {
        songView = (ListView) findViewById(R.id.song_list);     //links to ListView on xml

        Collections.sort(songList, new Comparator<Song>() {
            @Override
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());    //sorts songList
            }
        });

        SongAdapter songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);                   //this sets the songList into the ListView songView
    }


    private void getShuffle(){

        Cursor res = dbHelper.getStatus();          //gets cursor from database
        if(res.getCount() == 0){
            Toast.makeText(SongMenu.this, "Couldn't retrieve settings", Toast.LENGTH_LONG).toString();
            return;
        } else {
            while (res.moveToNext()) {
                String ciphertext = res.getString(0);       //pulls string from cursor
                shuffleStatus = ciphertext;
            }
        }
    }


    //when song is clicked on
    public void songPicked(View view) {
        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));       //sets song is MusicService
        musicSrv.playSong();                                                //plays selected song
        if (playbackPaused) {
            playbackPaused = false;
        }
        Intent I = new Intent(this, PlayScreen.class);                      //launches play screen
        startActivity(I);
    }


    //external memory permission callback
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case  MY_PERMISSIONS_REQUEST_READ_EXTERNAL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
