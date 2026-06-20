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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionnaire_notes_android.data.Note;
import com.example.gestionnaire_notes_android.data.NoteRepertoire;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private TextView tvAucuneNote;
    private FloatingActionButton fab;
    private NoteRepertoire repertoire;
    private EditText etRecherche;
    private Button btnFavoris;
    private TextView tvCompteur;
    private ImageButton btnTri;

    private ImageButton fabVert, fabRouge, fabBleu, fabJaune, fabOrange, fabGris;

    private boolean paletteVisible = false;
    private boolean filtreFavorisActif = false;
    private boolean triAlphabetique = false;

    // On garde toujours la liste complète en mémoire pour filtrer
    private List<Note> toutesLesNotes = new ArrayList<>();

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
        btnFavoris = findViewById(R.id.btnFavoris);
        etRecherche = findViewById(R.id.etRecherche);
        tvCompteur = findViewById(R.id.tvCompteur);
        btnTri = findViewById(R.id.btnTri);

        repertoire = new NoteRepertoire(getApplication());

        adapter = new NoteAdapter(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                Intent intent = new Intent(MainActivity.this, NoteFormActivity.class);
                intent.putExtra("note_id", note.getId());
                startActivity(intent);
            }

            @Override
            public void onFavoriToggle(Note note) {
                note.setFavori(!note.isFavori());
                repertoire.modifier(note);
                appliquerFiltres();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Observer toutes les notes — une seule fois, on garde la liste à jour
        repertoire.getToutesLesNotes().observe(this, notes -> {
            toutesLesNotes = notes != null ? notes : new ArrayList<>();
            appliquerFiltres();
        });

        fab.setOnClickListener(v -> togglePalette());

        fabVert.setOnClickListener(v -> lancerFormulaire("#219653"));
        fabRouge.setOnClickListener(v -> lancerFormulaire("#EB5757"));
        fabBleu.setOnClickListener(v -> lancerFormulaire("#2F80ED"));
        fabJaune.setOnClickListener(v -> lancerFormulaire("#F2C94C"));
        fabOrange.setOnClickListener(v -> lancerFormulaire("#F2994A"));
        fabGris.setOnClickListener(v -> lancerFormulaire("#828282"));

        btnFavoris.setOnClickListener(v -> {
            filtreFavorisActif = !filtreFavorisActif;
            btnFavoris.setAlpha(filtreFavorisActif ? 1f : 0.5f);
            appliquerFiltres();
        });

        btnTri.setOnClickListener(v -> {
            triAlphabetique = !triAlphabetique;
            btnTri.setImageResource(triAlphabetique ? 
                android.R.drawable.ic_menu_sort_alphabetically : 
                android.R.drawable.ic_menu_sort_by_size);
            appliquerFiltres();
        });

        setupSwipeToDelete();

        etRecherche.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                appliquerFiltres();
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    /**
     * Applique la recherche ET le filtre favoris en même temps.
     * C'est cette méthode qui centralise tout le filtrage.
     */
    private void appliquerFiltres() {
        String texteRecherche = etRecherche.getText().toString().trim().toLowerCase();
        List<Note> resultats = new ArrayList<>();

        for (Note note : toutesLesNotes) {
            boolean correspondRecherche = texteRecherche.isEmpty()
                    || (note.getTitre() != null && note.getTitre().toLowerCase().contains(texteRecherche));
            boolean correspondFavoris = !filtreFavorisActif || note.isFavori();

            if (correspondRecherche && correspondFavoris) {
                resultats.add(note);
            }
        }

        // Tri
        if (triAlphabetique) {
            Collections.sort(resultats, (n1, n2) -> {
                String t1 = n1.getTitre() != null ? n1.getTitre() : "";
                String t2 = n2.getTitre() != null ? n2.getTitre() : "";
                return t1.compareToIgnoreCase(t2);
            });
        } else {
            Collections.sort(resultats, (n1, n2) -> Long.compare(n2.getDateCreation(), n1.getDateCreation()));
        }

        afficherNotes(resultats);
    }

    private void afficherNotes(List<Note> notes) {
        adapter.setNotes(notes);
        tvAucuneNote.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
        tvCompteur.setText(notes.size() + (notes.size() > 1 ? " notes" : " note"));
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Note note = adapter.getNoteAt(position);

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Supprimer la note")
                        .setMessage("Voulez-vous vraiment supprimer cette note ?")
                        .setPositiveButton("Supprimer", (dialog, which) -> {
                            repertoire.supprimer(note);
                        })
                        .setNegativeButton("Annuler", (dialog, which) -> {
                            adapter.notifyItemChanged(position);
                        })
                        .show();
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
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