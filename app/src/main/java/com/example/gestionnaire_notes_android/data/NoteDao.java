package com.example.gestionnaire_notes_android.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void inserer(Note note);

    @Update
    void modifier(Note note);

    @Delete
    void supprimer(Note note);

    @Query("SELECT * FROM notes ORDER BY dateCreation DESC")
    LiveData<List<Note>> getToutesLesNotes();

    @Query("SELECT * FROM notes WHERE favori = 1 ORDER BY dateCreation DESC")
    LiveData<List<Note>> getFavoris();

    @Query("SELECT * FROM notes WHERE titre LIKE '%' || :recherche || '%' ORDER BY dateCreation DESC")
    LiveData<List<Note>> rechercherNotes(String recherche);
}