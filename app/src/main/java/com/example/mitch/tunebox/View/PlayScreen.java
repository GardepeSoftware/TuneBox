package com.example.mitch.tunebox.View;

import android.app.Activity;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mitch.tunebox.Model.ADT.AllMusic;
import com.example.mitch.tunebox.Model.AlbumArt;
import com.example.mitch.tunebox.Model.MusicController;
import com.example.mitch.tunebox.Model.MusicService;
import com.example.mitch.tunebox.Model.Singleton;
import com.example.mitch.tunebox.R;
import com.example.mitch.tunebox.Model.SQLiteHelper;
import com.example.mitch.tunebox.Model.ADT.SongArray;

/**
 * Created by Mitch on 9/29/16.
 */
public class PlayScreen extends Activity implements MediaController.MediaPlayerControl {
    private SongArray songList;
    private Intent playIntent;
    private SeekBar seekbar;
    private ImageView skipNext;
    private ImageView skipPrev;
    private ImageView playBtn;
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

        //build controller
        setController();

        //album art
        imgView = (ImageView)findViewById(R.id.imgAlbumArt);
        imgView.setScaleType(ImageView.ScaleType.FIT_XY);   //stretch image
        setAlbumArt();

        //music controllers
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        playBtn = (ImageView) findViewById(R.id.play_pause);
        skipNext = (ImageView) findViewById(R.id.next);
        skipPrev = (ImageView) findViewById(R.id.prev);

        //seekbar setup
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                leftNumbers.setText(DateUtils.formatElapsedTime(progress / 1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                stopSeekbarUpdate();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                getSupportMediaController().getTransportControls().seekTo(seekBar.getProgress());
                scheduleSeekbarUpdate();
            }
        });


        dbHelper = new SQLiteHelper(this);
    }

    public void setAlbumArt() {
        AlbumArt albumArt = new AlbumArt(albumID);

        Bitmap art = albumArt.getAlbumArt(this);
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
        if(paused){
            setController();
            paused = false;
        }
    }

    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }


    private void setController(){
        controller = new MusicController(this);           //passes current context to musicController class and returns it
        //set prev/next buttons with listeners
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.play_screen_frame));   //anchors the controller to a view
        controller.setEnabled(true);                                //sets the enabled state of the view
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
            setController();
            playbackPaused = false;
        }
    }

    //play previous
    private void playPrev(){
        musicSrv.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused = false;
        }
    }

    @Override
    public void start() {
        musicSrv.go();
    }           //etc...

    @Override
    public void pause() {
        playbackPaused = true;
        musicSrv.pausePlayer();
    }

    @Override
    public int getDuration() {
        if(musicSrv!=null &&  musicBound)
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {

        if(musicSrv!=null && musicBound){
            return musicSrv.getPosn();
        }
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound){
            return musicSrv.isPng();
        }
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() { return true; }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
