package com.example.gestionnaire_notes_android;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionnaire_notes_android.data.Note;
import com.example.gestionnaire_notes_android.data.NoteRepertoire;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteRepertoire noteRepertoire;
    private NoteAdapter adapter;
    private RecyclerView recyclerView;
    private TextView textAucuneNote;
    private EditText editSearch;
    private Button btnFavoris;
    private FloatingActionButton fabAjouter;

    private boolean filtreFavorisActif = false;

    // On garde toujours la liste complète en mémoire pour filtrer
    private List<Note> toutesLesNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteRepertoire = new NoteRepertoire(getApplication());

        recyclerView      = findViewById(R.id.recyclerViewNotes);
        textAucuneNote    = findViewById(R.id.textAucuneNote);
        editSearch        = findViewById(R.id.editSearch);
        btnFavoris        = findViewById(R.id.btnFavoris);
        fabAjouter        = findViewById(R.id.fabAjouter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NoteAdapter(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                // TODO Personne 3 : ouvrir NoteFormActivity en mode modification
            }

            @Override
            public void onFavoriToggle(Note note) {
                // Double clic détecté dans l'adapter → on bascule le favori
                note.setFavori(!note.isFavori());
                noteRepertoire.modifier(note);
                // On re-applique le filtre pour mettre à jour l'affichage immédiatement
                appliquerFiltres();
            }
        });

        recyclerView.setAdapter(adapter);

        // Observer toutes les notes — une seule fois, on garde la liste à jour
        noteRepertoire.getToutesLesNotes().observe(this, notes -> {
            toutesLesNotes = notes != null ? notes : new ArrayList<>();
            appliquerFiltres();
        });

        // Bouton FAB (Personne 4 gère la palette, on laisse vide pour l'instant)
        fabAjouter.setOnClickListener(v -> {
            // TODO Personne 4 : ouvrir palette de couleurs
        });

        // Bouton Favoris : activer/désactiver le filtre
        btnFavoris.setOnClickListener(v -> {
            filtreFavorisActif = !filtreFavorisActif;
            // Feedback visuel sur le bouton
            btnFavoris.setAlpha(filtreFavorisActif ? 1f : 0.5f);
            appliquerFiltres();
        });

        // Recherche en temps réel par titre
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                appliquerFiltres();
            }
        });
    }

    /**
     * Applique la recherche ET le filtre favoris en même temps.
     * C'est cette méthode qui centralise tout le filtrage.
     */
    private void appliquerFiltres() {
        String texteRecherche = editSearch.getText().toString().trim().toLowerCase();
        List<Note> resultats = new ArrayList<>();

        for (Note note : toutesLesNotes) {
            boolean correspondRecherche = texteRecherche.isEmpty()
                    || note.getTitre().toLowerCase().contains(texteRecherche);

            boolean correspondFavoris = !filtreFavorisActif || note.isFavori();

            if (correspondRecherche && correspondFavoris) {
                resultats.add(note);
            }
        }

        afficherNotes(resultats);
    }

    private void afficherNotes(List<Note> notes) {
        adapter.setNotes(notes);
        if (notes == null || notes.isEmpty()) {
            textAucuneNote.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textAucuneNote.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}