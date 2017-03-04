package com.example.mitch.tunebox.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.mitch.tunebox.CustomViews.AlbumSongsAdapter;
import com.example.mitch.tunebox.Model.MusicService;
import com.example.mitch.tunebox.Model.Singleton;
import com.example.mitch.tunebox.R;
import com.example.mitch.tunebox.Model.Song;
import com.example.mitch.tunebox.Model.SongArray;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Mitch on 9/30/16.
 */
public class AlbumsSongsMenu extends AppCompatActivity {
    private Singleton singleton;
    private SongArray albumSongs;
    private ListView albumSongsView;
    private MusicService musicSrv;
    private ServiceConnection musicConn;
    private Intent playIntent;
    public boolean paused = false;
    public boolean playbackPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_songs_menu);

        albumSongs = new SongArray();

        Bundle b = getIntent().getExtras();

        albumSongs = b.getParcelable("albumSongs");                 //gets albumSongs from prev activity

        singleton = Singleton.getInstance();
        playIntent = singleton.getPlayIntent();
        musicConn = singleton.getMusicConnection();                  //gets MusicSerivce related variables
        musicSrv = singleton.getService();


        albumSongsView = (ListView) findViewById(R.id.lstAlbumSongs);     //links to ListView on xml


        Collections.sort(albumSongs, new Comparator<Song>() {
            @Override
            public int compare(Song a, Song b) {
                return Long.compare(a.getTrackNo(), b.getTrackNo());
            }
        });


        AlbumSongsAdapter SongAdt = new AlbumSongsAdapter(this, albumSongs);
        albumSongsView.setAdapter(SongAdt);                   //this sets the albumsSongs to ListView



        Button BSong = (Button)findViewById(R.id.btnAlbumSongs);
        final View.OnClickListener OSongs = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ISong = new Intent(v.getContext(), SongMenu.class);          //launches SongMenu
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
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConn, Context.BIND_AUTO_CREATE);
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



    public void albumSongPicked(View view) {
        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));           //sets current song
        musicSrv.setList(albumSongs);
        musicSrv.playSong();
        if (playbackPaused) {
            playbackPaused = false;
        }

        int currIndex = Integer.parseInt(view.getTag().toString());
        Song currSong = albumSongs.get(currIndex);
        String song = currSong.title;
        String artistName = currSong.getArtist();
        String albumName = currSong.getAlbum();
        long albumID = currSong.getAlbumID();
        // Intent for the activity to open when user selects the notification
        Intent I = new Intent(this, PlayScreen.class);
        I.putExtra("albumID", albumID);
        I.putExtra("albumName", albumName);
        I.putExtra("artistName", artistName);
        I.putExtra("currSong", song);
        I.putExtra("playlist", (Parcelable)albumSongs);
        I.putExtra("songIndex", currIndex);
        I.putExtra("alreadyInitialized", false);
        startActivity(I);

/* Use TaskStackBuilder to build the back stack and get the PendingIntent
        PendingIntent pendingIntent =
                TaskStackBuilder.create(this)
                        // add all of DetailsActivity's parents to the stack,
                        // followed by DetailsActivity itself
                        .addNextIntentWithParentStack(upIntent)
                        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent);
        */
    }
}