package com.example.mitch.tunebox.Model.ADT;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Mitch on 9/19/16.
 */
public class AlbumArray extends ArrayList<Album> implements Parcelable {
    private String album;
    private ArrayList<Album> albumList;


    //constructs an artist from values
    public AlbumArray() {

    }

    public AlbumArray(Parcel in) {
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

            Album a = this.get(i);                      //turns Album into a parcel

            dest.writeString(a.getAlbum());
            dest.writeLong(a.getAlbumID());

        }
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AlbumArray createFromParcel(Parcel in) {
            return new AlbumArray(in);
        }

        public Object[] newArray(int arg0) {
            return null;
        }
    };


    private void readFromParcel(Parcel in) {

        this.clear();

        int size = in.readInt();

        for (int i = 0; i < size; i++) {            //gets Album from parcel and turns to string

            Album a = new Album(in.readString(), in.readLong());

            this.add(a);

        }
    }
}
