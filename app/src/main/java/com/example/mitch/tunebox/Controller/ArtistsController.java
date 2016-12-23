package com.example.mitch.tunebox.Controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.Signature;
import android.os.IBinder;

import com.example.mitch.tunebox.Model.ADT.AllMusic;
import com.example.mitch.tunebox.Model.GetMusic;
import com.example.mitch.tunebox.Model.MusicService;
import com.example.mitch.tunebox.Model.Singleton;

/**
 * Created by Mitch on 12/8/16.
 */

public class ArtistsController {
    private Context context;
    private Boolean musicBound;
    private Intent playIntent;
    private MusicService musicSrv;
    private Singleton singleton;

    public ArtistsController(Context c) {
        context = c;
        singleton = Singleton.getInstance();
    }

    //get music
    public AllMusic getMusic(){
        GetMusic getMusic = new GetMusic(context);
        AllMusic allMusic = getMusic.pullMusic();

        singleton.setAllMusic(allMusic);                    //stores entire library info in singleton

        return allMusic;
    }
}
