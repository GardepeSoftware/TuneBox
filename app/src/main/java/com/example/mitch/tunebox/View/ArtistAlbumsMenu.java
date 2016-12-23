package com.example.mitch.tunebox.View;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.mitch.tunebox.Controller.ArtistAlbumsController;
import com.example.mitch.tunebox.Model.ADT.Album;
import com.example.mitch.tunebox.Model.ADT.AlbumArray;
import com.example.mitch.tunebox.Model.ADT.AllMusic;
import com.example.mitch.tunebox.Model.AlbumArt;
import com.example.mitch.tunebox.Model.AlbumImageView;
import com.example.mitch.tunebox.Model.ArtistAlbumsAdapter;
import com.example.mitch.tunebox.Model.MusicService;
import com.example.mitch.tunebox.Model.Singleton;
import com.example.mitch.tunebox.R;
import com.example.mitch.tunebox.Model.ADT.Song;
import com.example.mitch.tunebox.Model.ADT.SongArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Mitch on 9/29/16.
 */
public class ArtistAlbumsMenu extends Activity {
    private AlbumArray artistAlbums;
    private SongArray albumSongs;
    private SongArray songList;
    private LinearLayout albumsView;
    public MusicService musicSrv;
    public boolean paused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_albums_menu);

        artistAlbums = new AlbumArray();
        songList = new SongArray();
        albumSongs = new SongArray();

        Bundle b = getIntent().getExtras();

        artistAlbums = b.getParcelable("artistAlbums");

        //get music
        ArtistAlbumsController controller = new ArtistAlbumsController();
        AllMusic allMusic = controller.getMusic();
        songList = allMusic.getSongList();
        albumsView = (LinearLayout) findViewById(R.id.albumArtLayout);

        setAlbumImages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            paused = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        musicSrv = null;
        super.onDestroy();
    }

    public void setAlbumImages(){
        //get artwork for albums
        AlbumArt albumArt = new AlbumArt(this);
        ArrayList<Bitmap> albumImages = albumArt.getArtistAlbums(artistAlbums);

        ArtistAlbumsAdapter a = new ArtistAlbumsAdapter(this);
        int j = 0;                      //decides which side of the ArtistAlbumsAdapter we're on
        //create image views
        for(int i = 0; i < albumImages.size(); i++) {
            if(j == 0) {
                a = new ArtistAlbumsAdapter(this);      //left side view
                a.setBitmap1(albumImages.get(i), i);
                j = 1;

                if(i == (albumImages.size() -1)){
                    a.initializeViews(this);            //last image in array
                    a.setImageViews();
                    a.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));
                    albumsView.addView(a);
                    j = 1;
                }
            } else {
                a.setBitmap2(albumImages.get(i), i);
                a.initializeViews(this);            //right side view
                a.setImageViews();
                a.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                albumsView.addView(a);
                j = 0;
            }
        }
    }


    public void artistAlbumPicked(View view) {
        int currView = Integer.parseInt(view.getTag().toString());              // get current album
        String currAlbum = artistAlbums.get(currView).getAlbum().toString();
        albumSongs.clear();

        for(int i= 0; i < (songList.size()-1); i++){
            if(songList.get(i).album.equals(currAlbum)){                        //get songs from current album;
                Song s = new Song(songList.get(i).getID(), songList.get(i).getTitle(),
                        songList.get(i).getTrackNo(), songList.get(i).getArtist(), songList.get(i).getAlbum(), songList.get(i).getArtistID(), songList.get(i).getAlbumID());
                albumSongs.add(s);

            }
        }
        Bundle b = new Bundle();
        b.putParcelable("albumSongs", albumSongs);                          //pass songs using bundle and launch AlbumSongsMenu
        Intent I = new Intent(this, AlbumsSongsMenu.class);
        I.putExtras(b);
        startActivity(I);
    }


}
