package com.example.elena.mynotes.ui.notes;

import com.example.elena.mynotes.MyNotesApp;
import com.example.elena.mynotes.database.MyNotesDao;
import com.example.elena.mynotes.database.entities.CategoryEntity;
import com.example.elena.mynotes.database.entities.NoteEntity;
import java.util.Date;
import java.util.List;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotesViewModel extends ViewModel {
    private final MyNotesDao mMyNotesDao;
    public MutableLiveData<List<NoteEntity>> notes = new MutableLiveData<>();
    public MutableLiveData<String> imageName = new MutableLiveData<>();
    public MutableLiveData<String> categoryName = new MutableLiveData<>();
    private int mCategoryId;
    private int mNoteId;
    private NoteEntity mNoteEntity;
    private boolean mIsSort = false;

    public NotesViewModel() {
        mMyNotesDao = MyNotesApp.getDatabase().myNotesDao();

    }

    public void load(int categoryId) {
        mCategoryId = categoryId;
        List<CategoryEntity> list = mMyNotesDao.categoryById(mCategoryId);
        if (!list.isEmpty()) {
            CategoryEntity categoryEntity = list.get(0);
            imageName.setValue(categoryEntity.imageName);
            categoryName.setValue(categoryEntity.name);
        }
        notes.setValue(mMyNotesDao.getNotesByCategoryId(mCategoryId));
    }

    public void createNote(String noteDescription) {
        NoteEntity noteEntity = new NoteEntity();
        noteEntity.categoryId = mCategoryId;
        noteEntity.description = noteDescription;
        Date now = new Date();
        noteEntity.creationDate = now;
        noteEntity.modifiedDate = now;
        mMyNotesDao.createNote(noteEntity);
        notes.setValue(mMyNotesDao.getNotesByCategoryId(mCategoryId));
    }

    public boolean switchNotesOrder() {
        mIsSort = !mIsSort;
        notes.setValue(mIsSort ? mMyNotesDao.getSortedNotesByCategoryId(mCategoryId) : mMyNotesDao.getNotesByCategoryId(mCategoryId));
        return mIsSort;
    }

    public void updateCategory(String newCategoryName, String newCategoryIcon) {
        CategoryEntity categoryEntity = mMyNotesDao.categoryById(mCategoryId).get(0);
        if (!newCategoryName.isEmpty() && !newCategoryName.equals(categoryName.getValue())) {
            categoryEntity.name = newCategoryName;
            categoryName.setValue(newCategoryName);
        }
        if (!newCategoryIcon.equals(imageName.getValue())) {
            categoryEntity.imageName = newCategoryIcon;
            imageName.setValue(newCategoryIcon);
        }
        mMyNotesDao.updateCategory(categoryEntity);
    }

    public void deleteCategory() {
        mMyNotesDao.deleteNotesByCategoryId(mCategoryId);
        mMyNotesDao.deleteCategory(mCategoryId);
    }

    public String noteDescription(int noteId) {
        mNoteId = noteId;
        mNoteEntity = mMyNotesDao.noteById(mNoteId).get(0);
        return mNoteEntity.description;
    }

    public void deleteNote() {
        mMyNotesDao.deleteNote(mNoteId);
        notes.setValue(mMyNotesDao.getNotesByCategoryId(mCategoryId));
    }

    public void updateNote(String newNoteDescription) {
        if (!newNoteDescription.equals(mNoteEntity.description)) {
            mNoteEntity.description = newNoteDescription;
            mNoteEntity.modifiedDate = new Date();
            mMyNotesDao.updateNote(mNoteEntity);
            notes.setValue(mMyNotesDao.getNotesByCategoryId(mCategoryId));
        }
    }
}
