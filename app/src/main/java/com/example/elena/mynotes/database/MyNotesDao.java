package com.example.elena.mynotes.database;

import com.example.elena.mynotes.database.entities.CategoryEntity;
import com.example.elena.mynotes.database.entities.NoteEntity;
import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MyNotesDao {

    // For CategoryEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void createCategory(CategoryEntity categoryEntity);

    @Update
    void updateCategory(CategoryEntity categoryEntity);

    @Delete
    void deleteCategory(CategoryEntity categoryEntity);

    @Query("DELETE from category_entity WHERE category_id = :categoryId")
    void deleteCategory(int categoryId);

    @Query("SELECT * from category_entity")
    List<CategoryEntity> getAllCategories();

    @Query("SELECT * from category_entity WHERE category_id = :categoryId")
    List<CategoryEntity> categoryById(int categoryId);


    // For NoteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createNote(NoteEntity noteEntity);

    @Update
    void updateNote(NoteEntity noteEntity);

    @Delete
    void deleteNote(NoteEntity noteEntity);

    @Query("DELETE from note_entity WHERE note_id = :noteId")
    void deleteNote(int noteId);

    @Query("SELECT * from note_entity")
    List<NoteEntity> getAllNotes();

    // Добавить метод получить все по categoryId!!!
    @Query("SELECT * from note_entity WHERE category_id = :categoryId")
    List<NoteEntity> getNotesByCategoryId(int categoryId);

    @Query("SELECT * from note_entity WHERE note_id = :noteId")
    List<NoteEntity> noteById(int noteId);

}
