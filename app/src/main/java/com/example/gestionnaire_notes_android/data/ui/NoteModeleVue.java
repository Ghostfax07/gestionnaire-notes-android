package com.example.gestionnaire_notes_android.data.ui;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.gestionnaire_notes_android.data.Note;
import com.example.gestionnaire_notes_android.data.NoteRepertoire;
import java.util.List;

public class NoteModeleVue extends AndroidViewModel {

    private NoteRepertoire repertoire;
    private LiveData<List<Note>> toutesLesNotes;

    public NoteModeleVue(Application application) {
        super(application);
        repertoire = new NoteRepertoire(application);
        toutesLesNotes = repertoire.getToutesLesNotes();
    }

    public void inserer(Note note) { repertoire.inserer(note); }

    public void modifier(Note note) { repertoire.modifier(note); }

    public void supprimer(Note note) { repertoire.supprimer(note); }

    public LiveData<List<Note>> getToutesLesNotes() { return toutesLesNotes; }

    public LiveData<List<Note>> getFavoris() { return repertoire.getFavoris(); }

    public LiveData<List<Note>> rechercherNotes(String recherche) {
        return repertoire.rechercherNotes(recherche);
    }
}