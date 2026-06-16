package com.example.gestionnaire_notes_android;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gestionnaire_notes_android.data.Note;
import com.example.gestionnaire_notes_android.data.NoteRepertoire;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteFormActivity extends AppCompatActivity {

    private EditText etTitre, etContenu;
    private String couleur = "#219653";
    private int noteId = -1;
    private NoteRepertoire repertoire;
    private Note noteExistante = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_form);

        etTitre = findViewById(R.id.etTitre);
        etContenu = findViewById(R.id.etContenu);
        Button btnSauvegarder = findViewById(R.id.btnSauvegarder);

        repertoire = new NoteRepertoire(getApplication());

        if (getIntent().hasExtra("couleur")) {
            couleur = getIntent().getStringExtra("couleur");
        }

        if (getIntent().hasExtra("note_id")) {
            noteId = getIntent().getIntExtra("note_id", -1);
            btnSauvegarder.setText("Modifier");
            // Charger la note existante
            repertoire.getToutesLesNotes().observe(this, notes -> {
                for (Note n : notes) {
                    if (n.getId() == noteId) {
                        noteExistante = n;
                        etTitre.setText(n.getTitre());
                        etContenu.setText(n.getContenu());
                        couleur = n.getCouleur();
                        etContenu.setBackgroundColor(Color.parseColor(couleur));
                        break;
                    }
                }
            });
        }

        etContenu.setBackgroundColor(Color.parseColor(couleur));

        btnSauvegarder.setOnClickListener(v -> sauvegarder());
    }

    private void sauvegarder() {
        String titre = etTitre.getText().toString().trim();
        String contenu = etContenu.getText().toString().trim();

        if (titre.isEmpty() && contenu.isEmpty()) {
            Toast.makeText(this, "La note ne peut pas être vide", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH).format(new Date());

        if (noteExistante != null) {
            noteExistante.setTitre(titre);
            noteExistante.setContenu(contenu);
            noteExistante.setCouleur(couleur);
            repertoire.modifier(noteExistante);
        } else {
            Note note = new Note(titre, contenu, couleur, false, date);
            repertoire.inserer(note);
        }

        finish();
    }
}