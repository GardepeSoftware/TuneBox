package com.example.mitch.tunebox.Controller;

import com.example.mitch.tunebox.Model.ADT.AllMusic;
import com.example.mitch.tunebox.Model.Singleton;

/**
 * Created by Mitch on 12/9/16.
 */

public class ArtistAlbumsController {
    private Singleton singleton;

    public AllMusic getMusic(){
        singleton = Singleton.getInstance();
        AllMusic allMusic = singleton.getAllMusic();

        return allMusic;
    }

}
