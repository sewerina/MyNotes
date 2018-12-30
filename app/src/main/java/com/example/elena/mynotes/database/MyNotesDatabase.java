package com.example.elena.mynotes.database;

import com.example.elena.mynotes.database.entities.CategoryEntity;
import com.example.elena.mynotes.database.entities.NoteEntity;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CategoryEntity.class, NoteEntity.class}, version = 1, exportSchema = false)
public abstract class MyNotesDatabase extends RoomDatabase {

    public abstract MyNotesDao myNotesDao();

}
