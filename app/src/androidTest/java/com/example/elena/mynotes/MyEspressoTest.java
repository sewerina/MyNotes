package com.example.elena.mynotes;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import com.example.elena.mynotes.database.MyNotesDatabase;
import com.example.elena.mynotes.ui.CategoriesActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MyEspressoTest {

    @Rule
    public ActivityTestRule<CategoriesActivity> mActivityRule =
            new ActivityTestRule<>(CategoriesActivity.class);

    @Before
    public void setUp() {
        // Context of the app under test.
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        MyNotesDatabase db = Room.inMemoryDatabaseBuilder(context, MyNotesDatabase.class)
                .allowMainThreadQueries()
                .build();
        MyNotesApp.setDatabase(db);
    }

    @Test
    public void testForAppName() {
        onView(withText("My notes")).check(matches(isDisplayed()));
    }

    @Test
    public void testForBtn() {
        onView(withId(R.id.fab_add_category)).check(matches(isDisplayed()));
        onView(withId(R.id.fab_add_category)).perform(click());
    }

    @Test
    public void testForDialogCreateCategory() {
        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.fab_add_category)).perform(click());

        onView(withText(R.string.dialog_title_createCategory)).check(matches(isDisplayed()));

        onView(withId(R.id.et_categoryName))
                .check(matches(instanceOf(EditText.class)))
                .check(matches(withHint(R.string.et_hint_categoryName)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.et_categoryName)).perform(typeText("Shop"));

        onView(withText("Ok"))
                .check(matches(instanceOf(Button.class)))
                .check(matches(isDisplayed()));

        onView(withText("Ok"))
                .perform(click())
                .check(doesNotExist());

        onView(withText("Shop")).check(matches(isDisplayed()));
    }

    @Test
    public void testForCategoryList() {
        onView(withId(R.id.recyclerView_categories)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerView_categories)).perform(click());
    }

}
