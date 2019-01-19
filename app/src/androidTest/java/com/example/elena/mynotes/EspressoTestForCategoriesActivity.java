package com.example.elena.mynotes;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.elena.mynotes.database.MyNotesDatabase;
import com.example.elena.mynotes.ui.categories.CategoriesActivity;
import com.example.elena.mynotes.ui.notes.CategoryActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.room.Room;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTestForCategoriesActivity {

    @Rule
    public IntentsTestRule<CategoriesActivity> mCategoriesActivityTestRule =
            new IntentsTestRule<>(CategoriesActivity.class, true, false);

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
        mCategoriesActivityTestRule.launchActivity(new Intent());
        onView(withText("My notes")).check(matches(isDisplayed()));
    }

    @Test
    public void testForCategoriesActivity() {
        mCategoriesActivityTestRule.launchActivity(new Intent());

        // FAB
        onView(withId(R.id.fab_add_category))
                .check(matches(instanceOf(FloatingActionButton.class)))
                .check(matches(isEnabled()))
                .check(matches(isDisplayed()))
                .perform(click());

        // Dialog title
        onView(withText(R.string.dialog_title_createCategory)).check(matches(isDisplayed()));

        // Dialog ET
        onView(withId(R.id.et_categoryName))
                .check(matches(instanceOf(EditText.class)))
                .check(matches(withHint(R.string.et_hint_categoryName)))
                .check(matches(isEnabled()))
                .check(matches(isDisplayed()));
        onView(withId(R.id.et_categoryName)).perform(typeText("Shop"), closeSoftKeyboard());

        // Dialog BTN for select icon
        onView(withId(R.id.btn_select_icon))
                .check(matches(instanceOf(Button.class)))
                .check(matches(withText(R.string.btn_categoryIcon)))
                .check(matches(isDisplayed()));
        onView(withId(R.id.btn_select_icon)).perform(click());

        // Check RadioBTN
        onView(withId(R.id.radioGroup))
                .check(matches(instanceOf(RadioGroup.class)))
                .check(matches(isDisplayed()));
        onView(withTagValue(is("shopping")))
                .check(matches(instanceOf(RadioButton.class)))
                .check(matches(isDisplayed()));
        onView(withTagValue(is("shopping"))).perform(click());

        // Dialog positive btn
        onView(withText(R.string.btn_ok))
                .check(matches(instanceOf(Button.class)))
                .check(matches(isDisplayed()));
        onView(withText(R.string.btn_ok))
                .perform(click())
                .check(doesNotExist());

        // Verify that the category is added in list
        onView(withText("Shop")).check(matches(isDisplayed()));
        onView(withId(R.id.iv_category))
                .check(matches(instanceOf(ImageView.class)))
                .check(matches(withTagValue(is("shopping"))))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recyclerView_categories)).check(matches(isDisplayed()));

        // Verification of intent
        onView(withText("Shop")).perform(click());
        intended(allOf(
                hasExtraWithKey(CategoryActivity.EXTRA_CATEGORY_ID)));
    }

}
