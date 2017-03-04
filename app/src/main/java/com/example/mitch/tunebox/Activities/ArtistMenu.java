package com.example.mitch.tunebox.Activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mitch.tunebox.Model.Album;
import com.example.mitch.tunebox.Model.AlbumArray;
import com.example.mitch.tunebox.Model.AllMusic;
import com.example.mitch.tunebox.Model.Artist;
import com.example.mitch.tunebox.CustomViews.ArtistAdapter;
import com.example.mitch.tunebox.Model.ArtistArray;
import com.example.mitch.tunebox.Model.GetMusic;
import com.example.mitch.tunebox.Model.MusicService;
import com.example.mitch.tunebox.Model.Singleton;
import com.example.mitch.tunebox.R;
import com.example.mitch.tunebox.Model.SongArray;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Mitch on 9/29/16.
 */
public class ArtistMenu extends AppCompatActivity {
    private ArtistArray artistList;
    private SongArray songList;
    private AlbumArray albumList;
    private AlbumArray artistAlbums;
    private ListView artistView;
    private Intent playIntent;
    private boolean musicBound=false;
    public MusicService musicSrv;
    public boolean paused = false;
    private Singleton singleton;
    private AllMusic allMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_menu);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if(!checkPermission()) {                //permission hasn't previously been given
            requestPermission();
        } else {
            permissionGranted();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_playscreen:
                goToPlayscreen();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void goToPlayscreen() {
        Intent intent = new Intent(getApplicationContext(), PlayScreen.class);
        intent.putExtra("alreadyInitialized", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);         //binds service to to intent
            startService(playIntent);
        }
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

    private Boolean checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                12000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionGranted();
        } else {
            Toast.makeText(getApplicationContext(), "This app needs permission to access external storage " +
                    "to play your music.", Toast.LENGTH_LONG).show();
            requestPermission();
        }
    }

    private void permissionGranted() {
        GetMusic getMusic = new GetMusic(getApplicationContext());
        allMusic = getMusic.pullMusic();

        artistList = allMusic.getArtistList();
        albumList = allMusic.getAlbumList();
        songList = allMusic.getSongList();
        artistAlbums = new AlbumArray();

        artistView = (ListView) findViewById(R.id.lstArtist);

        initialize();


        Collections.sort(artistList, new Comparator<Artist>() { //sorts songList
            @Override
            public int compare(Artist a, Artist b) {
                return a.getArtist().compareTo(b.getArtist());    //sort artists
            }
        });

        ArtistAdapter ArtistAdt = new ArtistAdapter(this, artistList);
        artistView.setAdapter(ArtistAdt);                   //maps artistList onto ListView
    }

    public void initialize(){
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);         //binds service to to intent
            startService(playIntent);
        }
    }

    //creates new service
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };


    //passes variables to application class for use in other classes
    public void setSingleton(){
        singleton = Singleton.getInstance();
        singleton.setService(musicSrv);
        singleton.setPlayIntent(playIntent);
        singleton.setMusicConnection(musicConnection);
        singleton.setAllMusic(allMusic);
    }

    private boolean albumInArray(AlbumArray list, Album elem){
        if(!list.isEmpty()){
            int i;
            for(i = 0; i < list.size(); i++){
                if(list.get(i).getAlbum().equals(elem.getAlbum())){
                    return true;
                }
            } return false;
        }
        return false;
    }


    public void artistPicked(View view) {
        int currView = Integer.parseInt(view.getTag().toString());
        String currArtist = artistList.get(currView).artist.toString();

        //need to reset artistAlbums
        artistAlbums.clear();

        for(int i= 0; i < (songList.size()-1); i++){
            if(songList.get(i).artist.equals(currArtist)){
                Album a = new Album(songList.get(i).album.toString(), songList.get(i).albumID,
                        songList.get(i).year);
                if(!albumInArray(artistAlbums, a)) {
                    artistAlbums.add(a);
                }
            }
        }

        setSingleton();
        Bundle b = new Bundle();
        b.putParcelable("artistAlbums", artistAlbums);
        Intent I = new Intent(this, ArtistAlbumsMenu.class);
        I.putExtras(b);                                             //put bundle into extras of intent
        startActivity(I);                                           //launch ArtistAlbumsMenu
    }
}