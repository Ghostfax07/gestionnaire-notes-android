package com.example.gestionnaire_notes_android;

import android.graphics.Color;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    public Note getNoteAt(int position) {
        return notes.get(position);
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
        android.util.Log.d("NoteDebug", "onBindViewHolder appelé pour : " + note.getTitre());

        holder.tvTitre.setText(note.getTitre());

        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy", Locale.FRENCH);
        holder.tvDate.setText(format.format(new Date(note.getDateCreation())));

        try {
            holder.itemView.setBackgroundColor(Color.parseColor(note.getCouleur()));
        } catch (Exception e) {
            holder.itemView.setBackgroundColor(Color.GRAY);
        }

        holder.ivFavori.setVisibility(note.isFavori() ? View.VISIBLE : View.GONE);

        // GestureDetector : distingue simple clic et double clic
        GestureDetector gestureDetector = new GestureDetector(
                holder.itemView.getContext(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        android.util.Log.d("NoteDebug", "onDown détecté");
                        return true;
                    }

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        android.util.Log.d("NoteDebug", "onSingleTapConfirmed -> onNoteClick");
                        listener.onNoteClick(note);
                        return true;
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        android.util.Log.d("NoteDebug", "onDoubleTap -> onFavoriToggle");
                        listener.onFavoriToggle(note);
                        return true;
                    }
                }
        );

        // holder.itemView.setOnClickListener(v -> {
        //    android.util.Log.d("NoteDebug", "CLIC SIMPLE reçu pour : " + note.getTitre());
        // });

        holder.itemView.setOnTouchListener((v, event) -> {
            boolean handled = gestureDetector.onTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_UP && !handled) {
                v.performClick();
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitre, tvDate;
        ImageView ivFavori;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitre = itemView.findViewById(R.id.tvTitre);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivFavori = itemView.findViewById(R.id.ivFavori);
        }
    }
}