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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Mitch on 2/25/17.
 */
@RunWith(AndroidJUnit4.class)
public class ArtistMenuTest {
    @Rule
    public ActivityTestRule<ArtistMenu> mActivityRule =
            new ActivityTestRule<>(ArtistMenu.class);

    @Test
    public void checkArtistListView() {
        onView(withId(R.id.lstArtist))
                .check(matches(isClickable()))
                .check(matches(isDisplayed()));
    }
}