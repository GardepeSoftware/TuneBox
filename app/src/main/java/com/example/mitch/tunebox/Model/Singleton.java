package com.example.mitch.tunebox.Model;

import android.content.Intent;
import android.content.ServiceConnection;

/**
 * Created by Mitch on 12/8/16.
 */
public class Singleton {
    private AllMusic allMusic;
    private MusicService musicSrv;
    private Intent playIntent;
    private String shuffleStatus;                                //this is an application class used for moving several variables between classes
    private ServiceConnection musicCon;
    private NowPlaying nowPlaying;


    private static Singleton ourInstance = new Singleton();

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
    }

    public void setAllMusic(AllMusic userAllMusic) {
        allMusic = userAllMusic;
    }

    public void setService(MusicService service)
        {
            this.musicSrv = service;
        }

    public void setPlayIntent(Intent intent){
            playIntent = intent;
        }

    public void setMusicConnection(ServiceConnection conn){
            musicCon = conn;
        }

    public void setShuffleStatus(String shuffle){
            shuffleStatus = shuffle;
        }

    public void setNowPlaying(NowPlaying nowPlayingInfo) {nowPlaying = nowPlayingInfo;}

    public AllMusic getAllMusic(){
        return allMusic;
    }

    public MusicService getService(){
            return musicSrv;
        }

    public Intent getPlayIntent(){
            return playIntent;
        }

    public ServiceConnection getMusicConnection(){
            return musicCon;
        }

    public String getShuffleStatus(){
            return shuffleStatus;
        }

    public NowPlaying getNowPlaying() {return nowPlaying;}

}
