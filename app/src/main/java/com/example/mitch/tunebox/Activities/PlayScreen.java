package com.example.mitch.tunebox.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mitch.tunebox.Model.AllMusic;
import com.example.mitch.tunebox.Model.AlbumArt;
import com.example.mitch.tunebox.Model.MusicService;
import com.example.mitch.tunebox.Model.NowPlaying;
import com.example.mitch.tunebox.Model.Singleton;
import com.example.mitch.tunebox.R;
import com.example.mitch.tunebox.Model.SQLiteHelper;
import com.example.mitch.tunebox.Model.SongArray;

import java.util.concurrent.TimeUnit;

/**
 * Created by Mitch on 9/29/16.
 */
public class PlayScreen extends AppCompatActivity {
    private Singleton singleton;
    private SongArray allSongList;
    private Intent playIntent;
    private SeekBar mSeekbar;
    private TextView txtViewCurrentPosition;
    private TextView txtViewDuration;
    private TextView txtViewArtist;
    private TextView txtViewSongTitle;
    private ImageView btnNext;
    private ImageView btnPrev;
    private ImageView btnPlay;
    private MusicService musicSrv;
    private ServiceConnection musicConn;
    private boolean musicBound=true;
    public boolean paused = false;
    public boolean playbackPaused =false;
    private String shuffleStatus= "false";
    private long albumID;
    private String artistName;
    private String albumName;
    private String currentSong;
    private SongArray playlist;
    private int songIndex;
    SQLiteHelper dbHelper;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_screen);

            init();
    }

    private Boolean checkIfReopening() {
        Intent i = getIntent();
        if(i.getBooleanExtra("alreadyInitialized", false)) {
            return true;
        }
        return false;
    }

    private void init() {
        if(!checkIfReopening()) {
            getExtras();
        } else {
            getNowPlaying();
        }
        getSingletonVars();
        setTrackLabels();
        passTextViews();
        setAlbumArt();
        setMusicController();
        setSeekBar();
        dbHelper = new SQLiteHelper(this);
        setNowPlaying();
    }

    private void getSingletonVars() {
        allSongList = new SongArray();
        singleton = Singleton.getInstance();
        playIntent = singleton.getPlayIntent();
        musicConn = singleton.getMusicConnection();
        musicSrv = singleton.getService();
        AllMusic allMusic = singleton.getAllMusic();
        allSongList = allMusic.getSongList();
    }

    private void getExtras() {
        Intent I = getIntent();
        albumID = I.getLongExtra("albumID", 55);
        artistName = I.getStringExtra("artistName");
        albumName = I.getStringExtra("albumName");
        currentSong = I.getStringExtra("currentSong");
        playlist = I.getParcelableExtra("playlist");
        songIndex = I.getIntExtra("songIndex", 1);
    }

    private void setTrackLabels() {
        txtViewSongTitle = (TextView) findViewById(R.id.songLabel);
        txtViewSongTitle.setText(playlist.get(songIndex).title);
        txtViewArtist = (TextView) findViewById(R.id.artistLabel);
        txtViewArtist.setText(artistName + " - " + albumName);
    }

    private void passTextViews() {
        musicSrv.passArtistAlbumTextView(txtViewArtist);
        musicSrv.passSongNameTextView(txtViewSongTitle);
        musicSrv.passDurationTextView(txtViewDuration);
    }
    public void setAlbumArt() {
        imgView = (ImageView)findViewById(R.id.imgAlbumArt);
        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
        AlbumArt albumArt = new AlbumArt(this);
        Bitmap art = albumArt.getAlbumArt(albumID);
        if(art != null) {
            imgView.setImageBitmap(art);        //get and set album art if found in music folder
        }
    }

    private void setMusicController() {
        mSeekbar = (SeekBar) findViewById(R.id.seekBar);
        txtViewCurrentPosition = (TextView) findViewById(R.id.startText);
        txtViewCurrentPosition.setText("00.00");
        txtViewDuration = (TextView) findViewById(R.id.endText);
        long duration = playlist.get(songIndex).duration;
        txtViewDuration.setText(toMinute(duration));
        musicSrv.passDurationTextView(txtViewDuration);
        btnPlay = (ImageView) findViewById(R.id.play_pause);
        btnNext = (ImageView) findViewById(R.id.next);
        btnPrev = (ImageView) findViewById(R.id.prev);
        setButtons();
    }

    private void setSeekBar() {
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(musicSrv != null && fromUser){
                    musicSrv.seek(progress*1000);       //changes place in song
                }
                txtViewCurrentPosition.setText(toMinute(progress*1000));
            }
        });

        final Handler mHandler = new Handler();
        PlayScreen.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(musicSrv != null){
                    mSeekbar.setMax(musicSrv.getDur()/1000);
                    int mCurrentPosition = musicSrv.getPosn() / 1000;
                    mSeekbar.setProgress(mCurrentPosition);         //updates seekbar on UI
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    private void setNowPlaying() {
        NowPlaying nowPlaying = new NowPlaying(playlist, songIndex, albumID, artistName, albumName,
                currentSong);
        singleton.setNowPlaying(nowPlaying);
    }

    private void getNowPlaying() {
        singleton = Singleton.getInstance();
        NowPlaying nowPlaying = singleton.getNowPlaying();
        albumID = nowPlaying.getAlbumID();
        artistName = nowPlaying.getArtist();
        albumName = nowPlaying.getAlbum();
        currentSong = nowPlaying.getTrackTitle();
        playlist = nowPlaying.getPlaylist();
        songIndex = nowPlaying.getSongIndex();
    }

    //all of the following are methods for the use of the MusicContoller class

    //play next
    private void playNext(){
        musicSrv.playNext();
        updateTrackInfo();
        if(playbackPaused){     //if music is paused and user hits next button, next song is played
            playbackPaused = false;
        }
    }

    //play previous
    private void playPrev(){
        musicSrv.playPrev();
        updateTrackInfo();
        if(playbackPaused){
            playbackPaused = false;
        }
    }

    private String toMinute(int time){
        long total = TimeUnit.MILLISECONDS.toSeconds(time);
        long minutes = TimeUnit.SECONDS.toMinutes(total);
        long remainSeconds= total - TimeUnit.MINUTES.toSeconds(minutes);
        String result = String.format("%02d", minutes) + ":"
                + String.format("%02d", remainSeconds);
        return result;
    }

    private String toMinute(long time) {
        long total = TimeUnit.MILLISECONDS.toSeconds(time);
        long minutes = TimeUnit.SECONDS.toMinutes(total);
        long remainSeconds= total - TimeUnit.MINUTES.toSeconds(minutes);
        String result = String.format("%02d", minutes) + ":"
                + String.format("%02d", remainSeconds);
        return result;
    }

    private void updateTrackInfo(){
        long duration = playlist.get(songIndex).duration;
        txtViewDuration.setText(toMinute(duration));
        txtViewSongTitle.setText(playlist.get(songIndex).title);
    }

    public void setButtons(){

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicSrv != null && musicBound){
                    if (musicSrv.isPng()) {
                        btnPlay.setImageResource(R.drawable.play_button);
                        musicSrv.pausePlayer();
                    } else {
                        btnPlay.setImageResource(R.drawable.pause_button);
                        musicSrv.go();
                    }
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
                songIndex++;
                updateTrackInfo();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
                songIndex--;
                updateTrackInfo();
            }
        });
    }

    private void setShuffle(){
        if(shuffleStatus.equals("false")) {
            dbHelper.updateStatus("true");                               //sets shuffle status
        } else if(shuffleStatus.equals("true")) {
            dbHelper.updateStatus("false");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_playscreen:

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }
}
