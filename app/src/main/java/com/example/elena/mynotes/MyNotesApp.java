package com.example.elena.mynotes;

import android.app.Application;
import android.content.Context;

import com.example.elena.mynotes.database.MyNotesDatabase;
import androidx.room.Room;

public class MyNotesApp extends Application {

    private static MyNotesDatabase sDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        sDatabase = Room.databaseBuilder(this,
                MyNotesDatabase.class, "MyNotesDatabase")
                .enableMultiInstanceInvalidation()
                .allowMainThreadQueries()
                .build();
    }

    public static MyNotesDatabase getDatabase() {
        return sDatabase;
    }

    public static void setDatabase(MyNotesDatabase db) {
        sDatabase = db;
    }
}
