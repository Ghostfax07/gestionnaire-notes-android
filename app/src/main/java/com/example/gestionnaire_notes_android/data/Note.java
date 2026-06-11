package com.example.gestionnaire_notes_android.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String titre;
    private String contenu;
    private String couleur;
    private boolean favori;
    private long dateCreation;

    // Constructeur
    public Note(String titre, String contenu, String couleur, boolean favori, long dateCreation) {
        this.titre = titre;
        this.contenu = contenu;
        this.couleur = couleur;
        this.favori = favori;
        this.dateCreation = dateCreation;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }

    public boolean isFavori() { return favori; }
    public void setFavori(boolean favori) { this.favori = favori; }

    public long getDateCreation() { return dateCreation; }
    public void setDateCreation(long dateCreation) { this.dateCreation = dateCreation; }
}