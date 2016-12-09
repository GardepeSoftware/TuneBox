package com.example.mitch.tunebox.Model.ADT;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Mitch on 9/29/16.
 */
public class ArtistArray extends ArrayList<Artist> implements Parcelable {
    private String artist;
    private ArrayList<Artist> artistList;


    //constructs an artist from values
    public ArtistArray(){

    }

    public ArtistArray(Parcel in){
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

            Artist a = this.get(i);                         //flatten artist to a parcel

            dest.writeString(a.getArtist());

        }
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public ArtistArray createFromParcel(Parcel in) {
            return new ArtistArray(in);
        }

        public Object[] newArray(int arg0) {
            return null;
        }
    };


    private void readFromParcel(Parcel in) {

        this.clear();

        int size = in.readInt();

        for (int i = 0; i < size; i++) {                        //turn parcel into an artist with string values

            Artist a = new Artist(in.readString());

            this.add(a);

        }
    }
}