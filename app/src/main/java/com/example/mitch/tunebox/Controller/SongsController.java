package com.example.mitch.tunebox.Controller;

import com.example.mitch.tunebox.Model.ADT.AllMusic;
import com.example.mitch.tunebox.Model.ADT.SongArray;
import com.example.mitch.tunebox.Model.Singleton;

/**
 * Created by Mitch on 12/8/16.
 */

public class SongsController {

    //return songList for song menu
    public SongArray getSongs(){
        Singleton singleton = Singleton.getInstance();
        AllMusic allMusic = singleton.getAllMusic();
        return allMusic.getSongList();
    }
}
