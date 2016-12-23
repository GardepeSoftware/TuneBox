package com.example.mitch.tunebox.Model;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.example.mitch.tunebox.Model.ADT.Song;
import com.example.mitch.tunebox.Model.ADT.SongArray;
import com.example.mitch.tunebox.R;
import com.example.mitch.tunebox.View.PlayScreen;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Mitch on 9/29/16.
 */
public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {//media player

    private MediaPlayer player;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPosn;
    private final IBinder musicBind = new MusicBinder();
    private String songTitle="";
    private static final int NOTIFY_ID=1;
    private boolean shuffle=false;
    private Random rand;

    @Override
    public void onCreate(){
        //create the service
        super.onCreate();
//initialize position
        songPosn=0;
//create player
        player = new MediaPlayer();
        rand=new Random();
        initMusicPlayer();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public void initMusicPlayer(){
        player.setWakeMode(getApplicationContext(),             //so music still plays when screen is off
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);   //sets player to stream music

        player.setOnPreparedListener(this);                     //when music source is ready
        player.setOnCompletionListener(this);                   //when song ends
        player.setOnErrorListener(this);                        //when there's an error
    }

    public void setList(ArrayList<Song> theSongs){
        songs=theSongs;
    }       //sets list of songs

    public void setSong(int songIndex){
        songPosn=songIndex;                                                 //picks song to be played
    }


    //added public in front of getService (in case of error)
    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }           //return this instance of the service
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        if(player.getCurrentPosition() > 0){
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

        Intent notIntent = new Intent(this, PlayScreen.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);     //puts current activity ontop of activity stack as new intent
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);  //if already exists, keep but refresh data

        Notification.Builder builder = new Notification.Builder(this);  //creates notification

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play_button)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    public void playSong(){
        player.reset();
        //get song
        Song playSong = songs.get(songPosn);
        songTitle=playSong.getTitle();

        long currSong = playSong.getID();                                           //gets songID

        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,       //gets uri for current song
                currSong);

        try{
            player.setDataSource(getApplicationContext(), trackUri);                //sets player up with song's uri
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();                                                      //begins music playback
    }

    //same as playSong only with different array to choose from
    public void playAlbumSong(SongArray albumSongs) {
        player.reset();

        Song playAlbumSong = albumSongs.get(songPosn);
        long currAlbumSong = playAlbumSong.getID();

        Uri albumTrackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currAlbumSong);

        try{
            player.setDataSource(getApplicationContext(), albumTrackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();

    }

    //when song ends or next button is pressed
    public void playNext(){
        if(shuffle){
            int newSong = songPosn;
            while(newSong==songPosn){
                newSong=rand.nextInt(songs.size());
            }
            songPosn=newSong;
        }
        songPosn++;                     //song index increments
        if(songPosn>=songs.size()) songPosn=0;
        playSong();
    }

    //when prev button is pressed
    public void playPrev(){
        songPosn--;                     //song index decrements
        if(songPosn<0) songPosn=songs.size()-1;
        playSong();
    }

    //current song position
    public int getPosn(){
        return player.getCurrentPosition();
    } //current position within song

    public int getDur(){
        return player.getDuration();
    }       //get song length

    public boolean isPng(){
        return player.isPlaying();
    }       //is song playing

    public void pausePlayer(){
        player.pause();
    }               //player is paused

    public void seek(int posn){
        player.seekTo(posn);
    }         //used for seeking within song

    public void go(){
        player.start();
    }

    public void setShuffle(){
        if(shuffle) shuffle=false;                          //changes shuffle status
        else shuffle=true;
    }

}
