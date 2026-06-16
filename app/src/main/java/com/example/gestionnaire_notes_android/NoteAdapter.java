package com.example.gestionnaire_notes_android;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> notes;
    private OnNoteClickListener listener;

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
        void onNoteDoubleClick(Note note);
    }

    public NoteAdapter(List<Note> notes, OnNoteClickListener listener) {
        this.notes = notes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);

        holder.tvTitre.setText(note.getTitre());
        holder.tvDate.setText(note.getDate());

        // Appliquer la couleur de fond
        holder.itemView.setBackgroundColor(Color.parseColor(note.getCouleur()));

        // Afficher l'icône favori
        if (note.isFavori()) {
            holder.ivFavori.setVisibility(View.VISIBLE);
        } else {
            holder.ivFavori.setVisibility(View.GONE);
        }

        // Clic simple → ouvrir modification
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onNoteClick(note);
        });

        // Double clic → toggle favori
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            private int clickCount = 0;
            private final android.os.Handler handler = new android.os.Handler();

            @Override
            public void onClick(View v) {
                clickCount++;
                if (clickCount == 1) {
                    handler.postDelayed(() -> {
                        if (clickCount == 1) {
                            if (listener != null) listener.onNoteClick(note);
                        } else {
                            if (listener != null) listener.onNoteDoubleClick(note);
                        }
                        clickCount = 0;
                    }, 300);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void updateNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitre, tvDate;
        ImageView ivFavori;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitre = itemView.findViewById(R.id.tvTitre);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivFavori = itemView.findViewById(R.id.ivFavori);
        }
    }
}