package com.example.mitch.tunebox.View;

import android.app.Activity;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.hardware.usb.UsbEndpoint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mitch.tunebox.Model.ADT.AllMusic;
import com.example.mitch.tunebox.Model.AlbumArt;
import com.example.mitch.tunebox.Model.MusicService;
import com.example.mitch.tunebox.Model.Singleton;
import com.example.mitch.tunebox.R;
import com.example.mitch.tunebox.Model.SQLiteHelper;
import com.example.mitch.tunebox.Model.ADT.SongArray;

import java.util.concurrent.TimeUnit;

/**
 * Created by Mitch on 9/29/16.
 */
public class PlayScreen extends Activity {
    private SongArray songList;
    private Intent playIntent;
    private SeekBar mSeekbar;
    private String seekbarStartPos;
    private String seekbarEndPos;
    private TextView startPos;
    private TextView endPos;
    private ImageView btnNext;
    private ImageView btnPrev;
    private ImageView btnPlay;
    private TextView leftNumbers;
    private TextView rightNumbers;
    private MusicService musicSrv;
    private ServiceConnection musicConn;
    private boolean musicBound=true;
    public boolean paused = false;
    public boolean playbackPaused =false;
    private String shuffleStatus= "false";
    private long albumID;
    private String artistName;
    private String albumName;
    private String currSong;
    SQLiteHelper dbHelper;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_screen);
        songList = new SongArray();

        Singleton singleton = Singleton.getInstance();
        playIntent = singleton.getPlayIntent();
        musicConn = singleton.getMusicConnection();
        musicSrv = singleton.getService();                       //gets service
        AllMusic allMusic = singleton.getAllMusic();
        songList = allMusic.getSongList();


        Intent I = getIntent();
        albumID = I.getLongExtra("albumID", 55);
        artistName = I.getStringExtra("artistName");
        albumName = I.getStringExtra("albumName");
        currSong = I.getStringExtra("currSong");

        //label texts
        TextView songLabel = (TextView) findViewById(R.id.songLabel);
        songLabel.setText(currSong);
        TextView artistLabel = (TextView) findViewById(R.id.artistLabel);
        artistLabel.setText(artistName);


        //album art
        imgView = (ImageView)findViewById(R.id.imgAlbumArt);
        imgView.setScaleType(ImageView.ScaleType.FIT_XY);   //stretch image
        setAlbumArt();

        //music controllers
        mSeekbar = (SeekBar) findViewById(R.id.seekBar);
        startPos = (TextView) findViewById(R.id.startText);
        endPos = (TextView) findViewById(R.id.endText);

        seekbarEndPos = toMinute(musicSrv.getDur());
        endPos.setText(seekbarEndPos);

        btnPlay = (ImageView) findViewById(R.id.play_pause);
        btnNext = (ImageView) findViewById(R.id.next);
        btnPrev = (ImageView) findViewById(R.id.prev);

        setButtons();

        //seekbar setup
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
                seekbarStartPos = toMinute(progress*1000);
               startPos.setText(seekbarStartPos);

            }
        });

        //set up runnable
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


        dbHelper = new SQLiteHelper(this);
    }




    public void setAlbumArt() {
        AlbumArt albumArt = new AlbumArt(this);

        Bitmap art = albumArt.getAlbumArt(albumID);
        if(art != null) {
            imgView.setImageBitmap(art);        //get and set album art if found in music folder
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                musicSrv.setShuffle();                                          //when shuffle is pressed
                setShuffle();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause(){
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }

    private void setShuffle(){
        if(shuffleStatus.equals("false")) {
            dbHelper.updateStatus("true");                               //sets shuffle status
        }else if(shuffleStatus.equals("true")) {
            dbHelper.updateStatus("false");
        }
    }

    //all of the following are methods for the use of the MusicContoller class

    //play next
    private void playNext(){
        musicSrv.playNext();
        if(playbackPaused){     //if music is paused and user hits next button, next song is played
            playbackPaused = false;
        }
    }

    //play previous
    private void playPrev(){
        musicSrv.playPrev();
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
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
    }
}
