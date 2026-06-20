package com.example.gestionnaire_notes_android.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteBDD extends RoomDatabase {

    private static NoteBDD instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteBDD getInstance(Context contexte) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            contexte.getApplicationContext(),
                            NoteBDD.class,
                            "base_de_donnees_notes")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}