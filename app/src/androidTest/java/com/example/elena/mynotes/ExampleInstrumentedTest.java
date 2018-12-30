package com.example.elena.mynotes;

import android.content.Context;
import com.example.elena.mynotes.database.MyNotesDao;
import com.example.elena.mynotes.database.MyNotesDatabase;
import com.example.elena.mynotes.database.entities.CategoryEntity;
import com.example.elena.mynotes.database.entities.NoteEntity;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private Context mAppContext;
    private MyNotesDatabase mDb;
    private MyNotesDao mMyNotesDao;

    @Before
    public void setUp() {
        // Context of the app under test.
        mAppContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        mDb = Room.inMemoryDatabaseBuilder(mAppContext, MyNotesDatabase.class)
                .allowMainThreadQueries()
                .build();

        mMyNotesDao = mDb.myNotesDao();
    }

    @Test
    public void testDatabaseCategoryEntity() {
        CategoryEntity categoryEntity1 = new CategoryEntity();
        categoryEntity1.id = 11;
        categoryEntity1.name = "training";

        CategoryEntity categoryEntity2 = new CategoryEntity();
        categoryEntity2.id = 22;
        categoryEntity2.name = "cleaning";

        mMyNotesDao.createCategory(categoryEntity1);
        mMyNotesDao.createCategory(categoryEntity2);

        assertEquals(2, mMyNotesDao.getAllCategories().size());
        assertEquals("training", mMyNotesDao.categoryById(11).get(0).name);

        categoryEntity2.name = "rest";
        mMyNotesDao.updateCategory(categoryEntity2);
        assertEquals("rest", mMyNotesDao.categoryById(22).get(0).name);

        CategoryEntity categoryEntity3 = new CategoryEntity();
        categoryEntity3.id = 33;
        categoryEntity3.name = "sleep";

        CategoryEntity categoryEntity4 = new CategoryEntity();
        categoryEntity4.id = 44;
        categoryEntity4.name = "run";

        CategoryEntity categoryEntity5 = new CategoryEntity();
        categoryEntity5.id = 55;
        categoryEntity5.name = "breakfast";

        mMyNotesDao.createCategory(categoryEntity3);
        mMyNotesDao.createCategory(categoryEntity4);
        mMyNotesDao.createCategory(categoryEntity5);
        assertEquals(5, mMyNotesDao.getAllCategories().size());

        mMyNotesDao.deleteCategory(categoryEntity3);
        assertEquals(4, mMyNotesDao.getAllCategories().size());
        assertTrue(mMyNotesDao.categoryById(33).isEmpty());

        mMyNotesDao.deleteCategory(44);
        assertEquals(3, mMyNotesDao.getAllCategories().size());
        assertTrue(mMyNotesDao.categoryById(44).isEmpty());
    }

    @Test
    public void testDatabaseNoteEntity() {
        NoteEntity entity1 = new NoteEntity();
        entity1.id = 1;
        entity1.categoryId = 44;
        entity1.description = "Run1";

        NoteEntity entity2 = new NoteEntity();
        entity2.id = 2;
        entity2.categoryId = 44;
        entity2.description = "Run2";

        NoteEntity entity3 = new NoteEntity();
        entity3.id = 3;
        entity3.categoryId = 66;
        entity3.description = "Fun";

        mMyNotesDao.createNote(entity1);
        mMyNotesDao.createNote(entity2);
        mMyNotesDao.createNote(entity3);

        assertEquals(3, mMyNotesDao.getAllNotes().size());
        assertEquals("Run2", mMyNotesDao.noteById(2).get(0).description);

        entity1.description = "Run run run run run";
        mMyNotesDao.updateNote(entity1);
        assertEquals("Run run run run run", mMyNotesDao.noteById(1).get(0).description);

        mMyNotesDao.deleteNote(entity1);
        assertEquals(2, mMyNotesDao.getAllNotes().size());
        assertTrue(mMyNotesDao.noteById(1).isEmpty());

        NoteEntity entity = new NoteEntity();
        entity.id = 7;
        mMyNotesDao.createNote(entity);
        assertEquals(3, mMyNotesDao.getAllNotes().size());
        mMyNotesDao.deleteNote(7);
        assertEquals(2, mMyNotesDao.getAllNotes().size());
        assertTrue(mMyNotesDao.noteById(7).isEmpty());

        entity.categoryId = 77;
        mMyNotesDao.createNote(entity1);
        mMyNotesDao.createNote(entity);
        assertEquals(4, mMyNotesDao.getAllNotes().size());
        assertEquals(2, mMyNotesDao.getNotesByCategoryId(44).size());
    }



}
