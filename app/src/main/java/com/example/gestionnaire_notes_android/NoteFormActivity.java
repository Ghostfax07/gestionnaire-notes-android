package com.example.gestionnaire_notes_android;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NoteFormActivity extends AppCompatActivity {

    private EditText etTitre, etContenu;
    private String couleur = "#219653";
    private int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_form);

        etTitre = findViewById(R.id.etTitre);
        etContenu = findViewById(R.id.etContenu);
        Button btnSauvegarder = findViewById(R.id.btnSauvegarder);

        // Récupérer la couleur et l'id depuis l'intent
        if (getIntent().hasExtra("couleur")) {
            couleur = getIntent().getStringExtra("couleur");
        }
        if (getIntent().hasExtra("note_id")) {
            noteId = getIntent().getIntExtra("note_id", -1);
            // TODO: charger la note existante via NoteRepository
            btnSauvegarder.setText("Modifier");
        }

        // Appliquer la couleur au fond
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

        // TODO: sauvegarder via NoteRepository
        finish();
    }
}