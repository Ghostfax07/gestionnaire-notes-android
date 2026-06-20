package com.example.gestionnaire_notes_android.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class NoteRepertoire {

    private NoteDao noteDao;
    private LiveData<List<Note>> toutesLesNotes;

    public NoteRepertoire(Application application) {
        NoteBDD base = NoteBDD.getInstance(application);
        noteDao = base.noteDao();
        toutesLesNotes = noteDao.getToutesLesNotes();
    }

    public LiveData<Note> getNoteParId(int id) {
        return noteDao.getNoteParId(id);
    }

    public void inserer(Note note) {
        new Thread(() -> noteDao.inserer(note)).start();
    }

    public void modifier(Note note) {
        new Thread(() -> noteDao.modifier(note)).start();
    }

    public void supprimer(Note note) {
        new Thread(() -> noteDao.supprimer(note)).start();
    }

    public LiveData<List<Note>> getToutesLesNotes() {
        return toutesLesNotes;
    }

    public LiveData<List<Note>> getFavoris() {
        return noteDao.getFavoris();
    }

    public LiveData<List<Note>> rechercherNotes(String recherche) {
        return noteDao.rechercherNotes(recherche);
    }
}