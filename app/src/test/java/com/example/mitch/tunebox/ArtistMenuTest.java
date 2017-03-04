package com.example.mitch.tunebox;

import com.example.mitch.tunebox.Activities.ArtistMenu;
import com.example.mitch.tunebox.Model.Album;
import com.example.mitch.tunebox.Model.AlbumArray;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Mitch on 2/23/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class ArtistMenuTest {

    private void albumInArray() {
        AlbumArray array = new AlbumArray();
        Album a = new Album("TestAlbum", 1L);
        array.add(a);
    }

}
