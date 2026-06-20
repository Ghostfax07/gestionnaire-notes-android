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

import java.util.concurrent.atomic.AtomicReference;

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

        // === AJOUTE CES LOGS ===
        android.util.Log.d("NoteForm", "🚀 onCreate appelé");

        int noteId = getIntent().getIntExtra("note_id", -1);
        android.util.Log.d("NoteForm", "📝 note_id reçu : " + noteId);

        AtomicReference<String> couleur = new AtomicReference<>(getIntent().getStringExtra("couleur"));
        android.util.Log.d("NoteForm", "🎨 couleur reçue : " + couleur);
        // === FIN DES LOGS ===

        layoutFond = findViewById(R.id.layoutFond);
        etTitre = findViewById(R.id.etTitre);
        etContenu = findViewById(R.id.etContenu);
        btnAction = findViewById(R.id.btnAction);

        repertoire = new NoteRepertoire(getApplication());

        // Récupérer la couleur (mode création depuis la palette)
        couleur.set(getIntent().getStringExtra("couleur"));
        if (couleur.get() == null) couleur.set("#219653");
        layoutFond.setBackgroundColor(Color.parseColor(couleur.get()));

        // Mode modification : on récupère l'id transmis par MainActivity
        noteId = getIntent().getIntExtra("note_id", -1);
        android.util.Log.d("NoteForm", "🔍 noteId après récupération : " + noteId);

        if (noteId != -1) {
            android.util.Log.d("NoteForm", "📖 Appel de getNoteParId(" + noteId + ")");
            int finalNoteId = noteId;
            repertoire.getNoteParId(noteId).observe(this, note -> {
                android.util.Log.d("NoteForm", "📦 Note reçue dans l'observateur : " + (note != null ? note.getTitre() : "null"));
                if (note != null) {
                    noteExistante = note;
                    etTitre.setText(note.getTitre());
                    etContenu.setText(note.getContenu());
                    couleur.set(note.getCouleur());
                    layoutFond.setBackgroundColor(Color.parseColor(couleur.get()));
                    btnAction.setText("Modifier");
                    android.util.Log.d("NoteForm", "✅ Note chargée : " + note.getTitre());
                } else {
                    android.util.Log.d("NoteForm", "❌ Note non trouvée pour l'id " + finalNoteId);
                }
            });
        } else {
            android.util.Log.d("NoteForm", "⚠️ Pas de note_id, mode création");
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
                        titre, contenu, couleur.get(), false,
                        System.currentTimeMillis()
                );
                repertoire.inserer(nouvelleNote);
            } else {
                // Modification
                noteExistante.setTitre(titre);
                noteExistante.setContenu(contenu);
                noteExistante.setCouleur(couleur.get());
                repertoire.modifier(noteExistante);
            }

            Toast.makeText(this, "Note enregistrée !", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}