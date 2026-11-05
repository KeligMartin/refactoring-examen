package fr.efrei.residence;

public class IdentifiantSequence {

    private int compteurEtudiants;
    private int compteurBaux;

    public String prochainEtudiant() {
        return "ETU-" + ++compteurEtudiants;
    }

    public String prochainBail() {
        return "BAIL-" + String.format("%03d", ++compteurBaux);
    }
}
