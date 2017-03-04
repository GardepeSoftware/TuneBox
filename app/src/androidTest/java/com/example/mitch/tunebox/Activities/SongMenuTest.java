package com.example.mitch.tunebox.Activities;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.mitch.tunebox.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by Mitch on 2/25/17.
 */
@RunWith(AndroidJUnit4.class)
public class SongMenuTest {

    @Rule
    public ActivityTestRule<SongMenu> mActivityRule =
            new ActivityTestRule<>(SongMenu.class);

    @Test
    public void checkSongsListView() {
        onView(withId(R.id.lstSongs))
                .check(matches(isClickable()))
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkSongsButtons() {
        onView(withId(R.id.btnSongs_Artists))
                .check(matches(isClickable()))
                .check(matches(isCompletelyDisplayed()));

        onView(withId(R.id.btnSongs_Artists))
                .check(matches(isClickable()))
                .check(matches(isCompletelyDisplayed()));

        onView(withId(R.id.btnSongs_Albums))
                .check(matches(isClickable()))
                .check(matches(isCompletelyDisplayed()));
    }
}