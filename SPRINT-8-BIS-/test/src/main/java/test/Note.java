package test;

public class Note {
    private String matiere;
    private double valeur;

    public String getMatiere() { return matiere; }
    public void setMatiere(String matiere) { this.matiere = matiere; }
    public double getValeur() { return valeur; }
    public void setValeur(double valeur) { this.valeur = valeur; }

    @Override
    public String toString() {
        return matiere + " : " + valeur;
    }
}
