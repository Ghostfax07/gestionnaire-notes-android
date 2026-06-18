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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteRepertoire = new NoteRepertoire(getApplication());

        recyclerView = findViewById(R.id.recyclerViewNotes);
        textAucuneNote = findViewById(R.id.textAucuneNote);
        editSearch = findViewById(R.id.editSearch);
        btnFavoris = findViewById(R.id.btnFavoris);
        fabAjouter = findViewById(R.id.fabAjouter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NoteAdapter(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
            }

            @Override
            public void onNoteLongClick(Note note) {
                note.setFavori(!note.isFavori());
                noteRepertoire.modifier(note);
            }
        });

        recyclerView.setAdapter(adapter);

        noteRepertoire.getToutesLesNotes().observe(this, this::afficherNotes);

        fabAjouter.setOnClickListener(v -> {
        });

        btnFavoris.setOnClickListener(v -> {
            filtreFavorisActif = !filtreFavorisActif;
            if (filtreFavorisActif) {
                noteRepertoire.getFavoris().observe(this, this::afficherNotes);
            } else {
                noteRepertoire.getToutesLesNotes().observe(this, this::afficherNotes);
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String texte = s.toString();
                if (texte.isEmpty()) {
                    noteRepertoire.getToutesLesNotes().observe(MainActivity.this, MainActivity.this::afficherNotes);
                } else {
                    noteRepertoire.rechercherNotes(texte).observe(MainActivity.this, MainActivity.this::afficherNotes);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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