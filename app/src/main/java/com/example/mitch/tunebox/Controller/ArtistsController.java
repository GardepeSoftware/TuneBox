package com.example.mitch.tunebox.Controller;

import android.content.Context;

import com.example.mitch.tunebox.Model.ADT.AllMusic;
import com.example.mitch.tunebox.Model.GetMusic;
import com.example.mitch.tunebox.Model.Singleton;

/**
 * Created by Mitch on 12/8/16.
 */

public class ArtistsController {
    private Context context;

    public ArtistsController(Context c) {
        context = c;
    }

    //get music
    public AllMusic getMusic(){
        GetMusic getMusic = new GetMusic(context);
        AllMusic allMusic = getMusic.pullMusic();

        Singleton singleton = Singleton.getInstance();
        singleton.setAllMusic(allMusic);                    //stores entire library info in singleton

        return allMusic;
    }
}
