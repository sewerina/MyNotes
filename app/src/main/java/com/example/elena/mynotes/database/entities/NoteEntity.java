package com.example.elena.mynotes.database.entities;

import com.example.elena.mynotes.database.DateConverter;
import java.util.Date;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "note_entity")
@TypeConverters(DateConverter.class)
public class NoteEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    public int id;

    @ForeignKey(entity = CategoryEntity.class, parentColumns = "category_id", childColumns = "category_id")
    @ColumnInfo(name = "category_id")
    public int categoryId;

    @ColumnInfo(name = "note_description")
    public String description;

    @ColumnInfo(name = "creation_date")
    public Date creationDate;

    @ColumnInfo(name = "modified_date")
    public Date modifiedDate;

}
