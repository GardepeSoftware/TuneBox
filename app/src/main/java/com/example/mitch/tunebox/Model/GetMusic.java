package com.example.mitch.tunebox.Model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.mitch.tunebox.Model.ADT.Album;
import com.example.mitch.tunebox.Model.ADT.AlbumArray;
import com.example.mitch.tunebox.Model.ADT.AllMusic;
import com.example.mitch.tunebox.Model.ADT.Artist;
import com.example.mitch.tunebox.Model.ADT.ArtistArray;
import com.example.mitch.tunebox.Model.ADT.Song;
import com.example.mitch.tunebox.Model.ADT.SongArray;

import java.util.ArrayList;

/**
 * Created by Mitch on 12/8/16.
 */

public class GetMusic {
    private Context context;

    public GetMusic(Context c) {
        context = c;
    }


    public AllMusic pullMusic() {

        SongArray songList = new SongArray();
        ArtistArray artistList = new ArtistArray();
        AlbumArray albumList = new AlbumArray();

        ContentResolver musicResolver = context.getContentResolver();       //returns ContentResolver for current app
        //MediaStore contains all meta data for media stored on internal external devices
        //we are accessing audio files
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        //cursors provide read-write access to files in musicUri
        Cursor musicCursor = musicResolver.query(musicUri, null, selection, null, null);


        //pulls musicCursor columns from first to last
        if (musicCursor != null && musicCursor.moveToFirst()) {                 //goes through queried results
            // get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int trackNo = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TRACK);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ALBUM);
            int albumIDColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);


                //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                int trackNumber = musicCursor.getInt(trackNo);
                long albumID = musicCursor.getLong(albumIDColumn);
                String thisTitle = musicCursor.getString(titleColumn);          //gets info from each song
                String thisArtist = musicCursor.getString(artistColumn);
                String thisAlbum = musicCursor.getString(albumColumn);
                int artistID = musicCursor.getInt(artistColumn);


                Song s = new Song(thisId, thisTitle, trackNumber, thisArtist, thisAlbum, artistID, albumID);  //uses info to create an object of type song
                songList.add(s);                                                        //new song is added to SongArray


                Artist a = new Artist(thisArtist);           //checks if artist is already in array
                if(!artistInArray(artistList, a)) {
                    artistList.add(a);                      //adds to at
                }


                Album album = new Album(thisAlbum, albumID);        //checks if album is already in array
                if(!albumInArray(albumList, album)) {
                    albumList.add(album);
                }

            }
            while (musicCursor.moveToNext());              //moves to next item if not null
        }

        //bundles all music together
        AllMusic allMusic = new AllMusic();
        allMusic.setSongList(songList);
        allMusic.setArtistList(artistList);
        allMusic.setAlbumList(albumList);

        return allMusic;
    }

    //checks if artist is already in array
    //prevents replication in artist list
    private boolean artistInArray(ArrayList<Artist> list, Artist elem){
        if(!list.isEmpty()){
            int i;
            for(i = 0; i < list.size(); i++){
                if(list.get(i).artist.equals(elem.artist)){
                    return true;
                }
            } return false;
        }
        return false;
    }

    //checks if album is already in array
    //prevents replication in album list
    private boolean albumInArray(ArrayList<Album> list, Album elem){
        if(!list.isEmpty()){
            int i;
            for(i = 0; i < list.size(); i++){
                if(list.get(i).getAlbum().equals(elem.getAlbum())){
                    return true;
                }
            } return false;
        }
        return false;
    }
}
