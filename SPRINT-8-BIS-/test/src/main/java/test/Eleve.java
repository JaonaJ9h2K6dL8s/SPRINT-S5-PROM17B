package test;

public class Eleve {
    private String nom;
    private int age;
    private Note note;

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public Note getNote() { return note; }
    public void setNote(Note note) { this.note = note; }

    @Override
    public String toString() {
        return "Eleve{nom='" + nom + "', age=" + age + ", note=" + note + "}";
    }
}
