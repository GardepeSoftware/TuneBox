package com.example.mitch.tunebox.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mitch.tunebox.Model.ADT.Album;
import com.example.mitch.tunebox.Model.ADT.AlbumArray;
import com.example.mitch.tunebox.Model.ApplicationClass;
import com.example.mitch.tunebox.Model.ArtistAlbumsAdapter;
import com.example.mitch.tunebox.Model.MusicService;
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
    private ArrayList<Album> artistAlbums;
    private SongArray albumSongs;
    private AlbumArray albumList;
    private SongArray songList;
    private ListView artistAlbumsView;
    public MusicService musicSrv;
    public boolean paused = false;
    ApplicationClass applicationClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_albums_menu);

        albumList = new AlbumArray();
        songList = new SongArray();
        artistAlbums = new AlbumArray();
        albumSongs = new SongArray();

        Bundle b = getIntent().getExtras();

        artistAlbums = b.getParcelable("artistAlbums");

        applicationClass = ((ApplicationClass)getApplicationContext());
        songList = applicationClass.getSongList();
        albumList = applicationClass.getAlbumList();

        artistAlbumsView = (ListView) findViewById(R.id.lstArtistAlbums);     //links to ListView on xml

        Toast.makeText(this, String.valueOf(artistAlbums.size()), Toast.LENGTH_SHORT).show();

        Collections.sort(artistAlbums, new Comparator<Album>() { //sorts songList
            @Override
            public int compare(Album a, Album b) {
                return a.getAlbum().compareTo(b.getAlbum());
            }
        });

        ArtistAlbumsAdapter AlbumAdt = new ArtistAlbumsAdapter(this, artistAlbums);
        artistAlbumsView.setAdapter(AlbumAdt);                   //maps artistAlbums to ListView

        Button BSong = (Button)findViewById(R.id.btnArtistSongs);
        final View.OnClickListener OSongs = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ISong = new Intent(v.getContext(), SongMenu.class);          //launches SongMenu
                startActivity(ISong);
            }
        };
        BSong.setOnClickListener(OSongs);



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


    public void albumPicked(View view) {
        int currView = Integer.parseInt(view.getTag().toString());              // get current album
        String currAlbum = artistAlbums.get(currView).getAlbum().toString();
        albumSongs.clear();

        for(int i= 0; i < (songList.size()-1); i++){
            if(songList.get(i).album.equals(currAlbum)){                        //get songs from current album;
                Song s = new Song(songList.get(i).getID(), songList.get(i).getTitle(),
                        songList.get(i).getArtist(), songList.get(i).getAlbum(), songList.get(i).getArtistID(), songList.get(i).getAlbumID());
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
