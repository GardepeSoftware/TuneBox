package com.example.mitch.tunebox;

/**
 * Created by Mitch on 2/21/17.
 */

import android.content.ContentResolver;
import android.content.Context;

import com.example.mitch.tunebox.Model.AllMusic;
import com.example.mitch.tunebox.Model.GetMusic;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Mitch on 2/21/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetMusicTest {
    @Mock
    Context context;

    @Mock
    ContentResolver contentResolver;

    @Test
    public void pullMusic() throws Exception {
        GetMusic getMusic = new GetMusic(context, contentResolver);
        AllMusic allMusic = getMusic.pullMusic();
        Assert.assertNotNull(allMusic);
        Assert.assertNotNull(allMusic.getArtistList());
        Assert.assertNotNull(allMusic.getAlbumList());
        Assert.assertNotNull(allMusic.getSongList());
    }
}