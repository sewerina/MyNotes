package com.example.elena.mynotes;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.elena.mynotes.database.MyNotesDatabase;
import com.example.elena.mynotes.database.entities.CategoryEntity;
import com.example.elena.mynotes.ui.CategoryActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EspressoTestForCategoryActivity {
    @Rule
    public ActivityTestRule<CategoryActivity> mCategoryActivityTestRule =
            new ActivityTestRule<>(CategoryActivity.class);

    @Before
    public void setUp() {
        // Context of the app under test.
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        MyNotesDatabase db = Room.inMemoryDatabaseBuilder(context, MyNotesDatabase.class)
                .allowMainThreadQueries()
                .build();
        MyNotesApp.setDatabase(db);

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.name = "Clearning";
        categoryEntity.imageName = "housekeeping";
        categoryEntity.id = 11;
        db.myNotesDao().createCategory(categoryEntity);

        Intent intent = new Intent()
                .putExtra(CategoryActivity.EXTRA_CATEGORY_NAME, categoryEntity.name)
                .putExtra(CategoryActivity.EXTRA_CATEGORY_ID, categoryEntity.id);
        mCategoryActivityTestRule.launchActivity(intent);
    }

    @Test
    public void testMenu() {
        // Edit menu item
        onView(withId(R.id.edit_category))
                .check(matches(instanceOf(ActionMenuItemView.class)))
                .check(matches(isEnabled()))
                .check(matches(isDisplayed()));

        // Sort menu item
        onView(withId(R.id.sort_notes))
                .check(matches(instanceOf(ActionMenuItemView.class)))
                .check(matches(isEnabled()))
                .check(matches(isDisplayed()));

        // Delete menu item
        onView(withId(R.id.delete_category))
                .check(matches(instanceOf(ActionMenuItemView.class)))
                .check(matches(isEnabled()))
                .check(matches(isDisplayed()));

        // Edit Category
        onView(withId(R.id.edit_category)).perform(click());
        onView(withText(R.string.dialog_title_updateCategory)).check(matches(isDisplayed()));

        onView(withId(R.id.et_categoryName))
                .check(matches(instanceOf(EditText.class)))
                .check(matches(withText("Clearning")))
                .check(matches(isEnabled()))
                .check(matches(isDisplayed()));
        onView(withId(R.id.et_categoryName)).perform(replaceText("My cat"));

        onView(withId(R.id.btn_select_icon))
                .check(matches(instanceOf(Button.class)))
                .check(matches(withText(R.string.btn_categoryIcon)))
                .check(matches(isEnabled()))
                .check(matches(isDisplayed()));
        onView(withId(R.id.btn_select_icon)).perform(click());

        onView(withId(R.id.radioGroup))
                .check(matches(instanceOf(RadioGroup.class)))
                .check(matches(isDisplayed()));
        onView(withTagValue(is("housekeeping")))
                .check(matches(instanceOf(RadioButton.class)))
                .check(matches(isChecked()))
                .check(matches(isDisplayed()));
        onView(withTagValue(is("cat")))
                .check(matches(instanceOf(RadioButton.class)))
                .check(matches(isDisplayed()))
                .check(matches(not(isChecked())))
                .perform(click());

        onView(withText(R.string.btn_ok))
                .check(matches(instanceOf(Button.class)))
                .check(matches(isEnabled()))
                .check(matches(isDisplayed()));
        onView(withText(R.string.btn_ok)).perform(click()).check(doesNotExist());

        onView(withText("My cat")).check(matches(isDisplayed()));

        // Delete Category: At first add some notes here, then delete the category.
        onView(withId(R.id.fab_add_note)).perform(click());
        onView(withId(R.id.et_note)).perform(typeText("toy"));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.fab_add_note)).perform(click());
        onView(withId(R.id.et_note)).perform(typeText("Favourite food"));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.fab_add_note)).perform(click());
        onView(withId(R.id.et_note)).perform(typeText("The cat bed"));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.delete_category)).perform(click());
        onView(withText("toy")).check(doesNotExist());
        onView(withText("Favourite food")).check(doesNotExist());
        onView(withText("The cat bed")).check(doesNotExist());
        List<CategoryEntity> categories = MyNotesApp.getDatabase().myNotesDao().getAllCategories();
        assertTrue(categories.isEmpty());


    }

    @Test
    public void testForCategoryActivity() {
        // Title
        onView(withText("Clearning")).check(matches(isDisplayed()));

        // FAB
        onView(withId(R.id.fab_add_note))
                .check(matches(instanceOf(FloatingActionButton.class)))
                .check(matches(isEnabled()))
                .check(matches(isDisplayed()))
                .perform(click());

        // Dialog title
        onView(withText(R.string.dialog_title_createNote)).check(matches(isDisplayed()));

        // Dialog ET
        onView(withId(R.id.et_note))
                .check(matches(instanceOf(EditText.class)))
                .check(matches(withHint(R.string.et_hint_note)))
                .check(matches(isDisplayed()));
        onView(withId(R.id.et_note)).perform(typeText("Window"));

        // Dialog positive btn
        onView(withId(android.R.id.button1))
                .check(matches(instanceOf(Button.class)))
                .check(matches(isEnabled()))
                .check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click()).check(doesNotExist());

        // Add item in the note list
        onView(withText("Window")).check(matches(isDisplayed()));
        onView(withId(R.id.tv_creationDate))
                .check(matches(instanceOf(TextView.class)))
                .check(matches(withText(not(isEmptyString()))))
                .check(matches(withText(endsWith("2019 г."))))
                .check(matches(isDisplayed()));
        onView(withId(R.id.tv_editedDate))
                .check(matches(instanceOf(TextView.class)))
                .check(matches(withText(not(isEmptyString()))))
                .check(matches(withText(endsWith("2019 г."))))
                .check(matches(isDisplayed()));

        // Update note in list


        // Delete note from list



    }

}
