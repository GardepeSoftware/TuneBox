package com.example.mitch.tunebox.Model.ADT;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Mitch on 9/29/16.
 */
public class SongArray extends ArrayList<Song> implements Parcelable {
    private long id;
    private String song;
    private String artist;
    private String album;
    private int artistID;

    private ArrayList<Song> SongList;


    //constructs an artist from values
    public SongArray(){

    }

    public SongArray(Parcel in){
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int size = this.size();

        dest.writeInt(size);
        for (int i = 0; i < size; i++) {

            Song s = this.get(i);

            dest.writeLong(s.getID());
            dest.writeString(s.getTitle());                 //flattens song to a parcel
            dest.writeInt(s.getTrackNo());
            dest.writeString(s.getArtist());
            dest.writeString(s.getAlbum());
            dest.writeInt(s.getArtistID());
            dest.writeLong(s.getAlbumID());
        }
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public SongArray createFromParcel(Parcel in) {
            return new SongArray(in);
        }

        public Object[] newArray(int arg0) {
            return null;
        }
    };


    private void readFromParcel(Parcel in) {

        this.clear();

        int size = in.readInt();

        for (int i = 0; i < size; i++) {                    //turns song from parcel to String(s)

            Song s = new Song(in.readLong(),in.readString(), in.readInt(), in.readString(),in.readString(),in.readInt(), in.readLong());

            this.add(s);

        }
    }
}