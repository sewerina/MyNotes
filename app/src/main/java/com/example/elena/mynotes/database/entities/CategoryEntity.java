package com.example.elena.mynotes.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_entity")
public class CategoryEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    public int id;

    @ColumnInfo(name = "category_name")
    public String name;

    @ColumnInfo(name = "category_image")
    public int imageResource;

}
