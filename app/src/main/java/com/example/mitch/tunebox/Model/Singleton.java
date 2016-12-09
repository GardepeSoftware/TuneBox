package com.example.mitch.tunebox.Model;

import android.app.Application;
import android.content.Intent;
import android.content.ServiceConnection;

import com.example.mitch.tunebox.Model.ADT.AllMusic;

/**
 * Created by Mitch on 12/8/16.
 */
public class Singleton {
    private AllMusic allMusic;
    private MusicService musicSrv;
    private Intent playIntent;
    public String shuffleStatus;                                //this is an application class used for moving several variables between classes
    ServiceConnection musicCon;


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

}
