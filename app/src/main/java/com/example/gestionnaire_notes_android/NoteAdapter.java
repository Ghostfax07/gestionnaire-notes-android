package com.example.gestionnaire_notes_android;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestionnaire_notes_android.data.Note;
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


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionnaire_notes_android.data.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
        void onFavoriToggle(Note note); // double clic → toggle favori
    }

    private List<Note> notes = new ArrayList<>();
    private final OnNoteClickListener listener;

    public NoteAdapter(OnNoteClickListener listener) {
        this.listener = listener;
    }

    public void setNotes(List<Note> nouvellesNotes) {
        this.notes = nouvellesNotes != null ? nouvellesNotes : new ArrayList<>();
        notifyDataSetChanged();
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
        holder.itemView.setBackgroundColor(Color.parseColor(note.getCouleur()));

        if (note.isFavori()) {
            holder.ivFavori.setVisibility(View.VISIBLE);
        } else {
            holder.ivFavori.setVisibility(View.GONE);
        }

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
        // Titre
        holder.textTitre.setText(note.getTitre());

        // Date
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy", Locale.FRENCH);
        holder.textDate.setText(format.format(note.getDateCreation()));

        // Couleur de fond
        try {
            holder.cardLayout.setBackgroundColor(Color.parseColor(note.getCouleur()));
        } catch (Exception e) {
            holder.cardLayout.setBackgroundColor(Color.GRAY);
        }

        // Icône étoile : visible si favori
        holder.iconFavori.setVisibility(note.isFavori() ? View.VISIBLE : View.GONE);

        // GestureDetector : distingue simple clic et double clic
        GestureDetector gestureDetector = new GestureDetector(
                holder.itemView.getContext(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        // Simple clic → ouvrir la note
                        listener.onNoteClick(note);
                        return true;
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        // Double clic → toggler le favori
                        listener.onFavoriToggle(note);
                        return true;
                    }
                }
        );

        holder.itemView.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            // On retourne false pour ne pas bloquer les events natifs (ripple, etc.)
            return false;
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
    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textTitre, textDate;
        ImageView iconFavori;
        View cardLayout;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitre  = itemView.findViewById(R.id.textTitre);
            textDate   = itemView.findViewById(R.id.textDate);
            iconFavori = itemView.findViewById(R.id.iconFavori);
            cardLayout = itemView;
        }
    }
}
