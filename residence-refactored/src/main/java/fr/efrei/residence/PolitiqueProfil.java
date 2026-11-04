package fr.efrei.residence;

public interface PolitiqueProfil {
    double appliquerRemise(double loyer, int echelon);
    boolean estSolvable(double ressources, double loyerCc);
    double depotGarantie(double loyerHc);
    double fraisDossier();
    int dureePreavisJours();
    double penaliteSiRetard(double loyerCc);
    String motifRefusInscription(int echelon, String telephone);
    boolean recoitEmailComplementaire(String canal);

    static PolitiqueProfil pour(String profil) {
        return switch (profil) {
            case "STANDARD" -> new ProfilStandard();
            case "BOURSIER" -> new ProfilBoursier();
            case "ALTERNANT" -> new ProfilAlternant();
            case "INTERNATIONAL" -> new ProfilInternational();
            default -> null;
        };
    }
}
