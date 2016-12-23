package com.example.mitch.tunebox.Model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.example.mitch.tunebox.Model.ADT.AlbumArray;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Mitch on 12/5/16.
 */

public class AlbumArt {
    private Long albumID;
    private Context context;

    public AlbumArt(Context activityContext){
        context = activityContext;
    }


    public Bitmap getAlbumArt(Long userAlbumID){
        Bitmap artwork = null;

        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri, userAlbumID);
        ContentResolver res = context.getContentResolver();

        try {
            InputStream in = res.openInputStream(uri);
            artwork = BitmapFactory.decodeStream(in);
        } catch (Exception E) {

        }

        return artwork;

        /*
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = musicResolver.query(musicUri, null, MediaStore.Audio.Media.ALBUM_ID + " = " +
                userAlbumID, null, null);

        if(cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            metaRetriever = new MediaMetadataRetriever();
            metaRetriever.setDataSource(path);

            try {
                art = metaRetriever.getEmbeddedPicture();
                Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
            } catch (Exception e) {
            }
        }

        try {
            album = URLEncoder.encode(albumName, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            // Can be safely ignored because UTF-8 is always supported
        }

        try {
            song = URLEncoder.encode(currSong, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            // Can be safely ignored because UTF-8 is always supported
        }

        try {
            artist = URLEncoder.encode(artistName, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            // Can be safely ignored because UTF-8 is always supported
        }


       */
    }

    public ArrayList<Bitmap> getArtistAlbums(AlbumArray albums){
        ArrayList<Bitmap> albumImages = new ArrayList<Bitmap>();

        for(int i = 0; i < albums.size(); i++) {
            Long userAlbumID = albums.get(i).getAlbumID();
            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(sArtworkUri, userAlbumID);
            ContentResolver res = context.getContentResolver();

            try {
                InputStream in = res.openInputStream(uri);
                albumImages.add(BitmapFactory.decodeStream(in));
            } catch (Exception E) {

            }
        }

        return albumImages;
    }
}
