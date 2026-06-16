package com.example.gestionnaire_notes_android;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import com.example.gestionnaire_notes_android.data.Note;
import com.example.gestionnaire_notes_android.data.NoteRepertoire;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private TextView tvAucuneNote;
    private FloatingActionButton fab;
    private NoteRepertoire repertoire;
    private boolean paletteVisible = false;
    private boolean showingFavoris = false;
    private EditText etRecherche;

    private ImageButton fabVert, fabRouge, fabBleu, fabJaune, fabOrange, fabGris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        tvAucuneNote = findViewById(R.id.tvAucuneNote);
        fab = findViewById(R.id.fab);
        fabVert = findViewById(R.id.fabVert);
        fabRouge = findViewById(R.id.fabRouge);
        fabBleu = findViewById(R.id.fabBleu);
        fabJaune = findViewById(R.id.fabJaune);
        fabOrange = findViewById(R.id.fabOrange);
        fabGris = findViewById(R.id.fabGris);
        Button btnFavoris = findViewById(R.id.btnFavoris);
        etRecherche = findViewById(R.id.etRecherche);

        repertoire = new NoteRepertoire(getApplication());

        adapter = new NoteAdapter(new ArrayList<>(), new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                Intent intent = new Intent(MainActivity.this, NoteFormActivity.class);
                intent.putExtra("note_id", note.getId());
                startActivity(intent);
            }
            @Override
            public void onNoteDoubleClick(Note note) {
                note.setFavori(!note.isFavori());
                repertoire.modifier(note);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Observer toutes les notes
        repertoire.getToutesLesNotes().observe(this, notes -> {
            if (!showingFavoris) {
                String query = etRecherche.getText().toString();
                filtrerLocalement(notes, query);
            }
        });

        fab.setOnClickListener(v -> togglePalette());

        fabVert.setOnClickListener(v -> lancerFormulaire("#219653"));
        fabRouge.setOnClickListener(v -> lancerFormulaire("#EB5757"));
        fabBleu.setOnClickListener(v -> lancerFormulaire("#2F80ED"));
        fabJaune.setOnClickListener(v -> lancerFormulaire("#F2C94C"));
        fabOrange.setOnClickListener(v -> lancerFormulaire("#F2994A"));
        fabGris.setOnClickListener(v -> lancerFormulaire("#828282"));

        btnFavoris.setOnClickListener(v -> {
            showingFavoris = !showingFavoris;
            if (showingFavoris) {
                repertoire.getFavoris().observe(this, notes -> {
                    adapter.updateNotes(notes);
                    tvAucuneNote.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } else {
                repertoire.getToutesLesNotes().observe(this, notes -> {
                    filtrerLocalement(notes, etRecherche.getText().toString());
                });
            }
        });

        etRecherche.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                repertoire.getToutesLesNotes().observe(MainActivity.this, notes -> {
                    filtrerLocalement(notes, s.toString());
                });
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void filtrerLocalement(List<Note> notes, String query) {
        List<Note> filtered = new ArrayList<>();
        for (Note note : notes) {
            if (note.getTitre().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(note);
            }
        }
        adapter.updateNotes(filtered);
        tvAucuneNote.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void togglePalette() {
        paletteVisible = !paletteVisible;
        int visibility = paletteVisible ? View.VISIBLE : View.GONE;
        fabVert.setVisibility(visibility);
        fabRouge.setVisibility(visibility);
        fabBleu.setVisibility(visibility);
        fabJaune.setVisibility(visibility);
        fabOrange.setVisibility(visibility);
        fabGris.setVisibility(visibility);
    }

    private void lancerFormulaire(String couleur) {
        togglePalette();
        Intent intent = new Intent(this, NoteFormActivity.class);
        intent.putExtra("couleur", couleur);
        startActivity(intent);
    }
}