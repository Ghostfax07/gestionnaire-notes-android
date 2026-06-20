package com.example.gestionnaire_notes_android;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestionnaire_notes_android.data.Note;
import com.example.gestionnaire_notes_android.data.NoteRepertoire;

public class NoteFormActivity extends AppCompatActivity {

    private LinearLayout layoutFond;
    private EditText etTitre, etContenu;
    private Button btnAction;
    private String couleur;
    private NoteRepertoire repertoire;
    private Note noteExistante;
    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_form);

        noteId = getIntent().getIntExtra("note_id", -1);
        this.couleur = getIntent().getStringExtra("couleur");

        layoutFond = findViewById(R.id.layoutFond);
        etTitre = findViewById(R.id.etTitre);
        etContenu = findViewById(R.id.etContenu);
        btnAction = findViewById(R.id.btnAction);

        repertoire = new NoteRepertoire(getApplication());

        // Récupérer la couleur (mode création depuis la palette)
        if (this.couleur == null) this.couleur = "#219653";
        layoutFond.setBackgroundColor(Color.parseColor(this.couleur));

        // Mode modification : on récupère l'id transmis par MainActivity
        if (noteId != -1) {
            repertoire.getNoteParId(noteId).observe(this, note -> {
                if (note != null) {
                    noteExistante = note;
                    etTitre.setText(note.getTitre());
                    etContenu.setText(note.getContenu());
                    this.couleur = note.getCouleur();
                    layoutFond.setBackgroundColor(Color.parseColor(this.couleur));
                    btnAction.setText("Modifier");
                }
            });
        }


        btnAction.setOnClickListener(v -> {
            String titre   = etTitre.getText().toString().trim();
            String contenu = etContenu.getText().toString().trim();

            if (titre.isEmpty() || contenu.isEmpty()) {
                Toast.makeText(this,
                        "Le titre et le contenu sont obligatoires",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (noteExistante == null) {
                // Création
                Note nouvelleNote = new Note(
                        titre, contenu, this.couleur, false,
                        System.currentTimeMillis()
                );
                repertoire.inserer(nouvelleNote);
            } else {
                // Modification
                noteExistante.setTitre(titre);
                noteExistante.setContenu(contenu);
                noteExistante.setCouleur(this.couleur);
                repertoire.modifier(noteExistante);
            }

            Toast.makeText(this, "Note enregistrée !", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}