package fr.efrei.residence;

public enum OptionLoyer {
    MENAGE("ménage"),
    PARKING("parking"),
    LAVERIE("laverie"),
    FIBRE("fibre"),
    ASSURANCE("assurance");

    private final String libelle;

    OptionLoyer(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
