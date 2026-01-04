package com.monframework;


public class FileUpload {
    private byte[] donnees;
    private String nomFichier;
    private long taille;

    public FileUpload() {}

    public FileUpload(byte[] donnees, String nomFichier, long taille) {
        this.donnees = donnees;
        this.nomFichier = nomFichier;
        this.taille = taille;
    }

    // Getters et Setters
    public byte[] getDonnees() {
        return donnees;
    }

    public void setDonnees(byte[] donnees) {
        this.donnees = donnees;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public long getTaille() {
        return taille;
    }

    public void setTaille(long taille) {
        this.taille = taille;
    }

    @Override
    public String toString() {
        return "FileUpload{" +
                "nomFichier='" + nomFichier + '\'' +
                ", taille=" + taille + " bytes" +
                '}';
    }
}
