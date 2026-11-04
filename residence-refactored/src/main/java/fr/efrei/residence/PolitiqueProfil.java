package fr.efrei.residence;

public interface PolitiqueProfil {
    double TAUX_PENALITE_RETARD = 0.05;
    double PENALITE_MINIMUM = 10;
    int SOLVABILITE_RATIO_DEFAUT = 3;
    double FRAIS_DOSSIER_DEFAUT = 100;
    int PREAVIS_JOURS_COURT = 15;
    int PREAVIS_JOURS_LONG = 30;

    double appliquerRemise(double loyer, int echelon);
    boolean estSolvable(double ressources, double loyerCc);
    double depotGarantie(double loyerHc);
    double fraisDossier();
    int dureePreavisJours();
    double penaliteSiRetard(double loyerCc);
    String motifRefusInscription(int echelon, String telephone);
    boolean recoitEmailComplementaire(CanalNotification canal);

    static PolitiqueProfil pour(ProfilEtudiant profil) {
        return switch (profil) {
            case STANDARD -> new ProfilStandard();
            case BOURSIER -> new ProfilBoursier();
            case ALTERNANT -> new ProfilAlternant();
            case INTERNATIONAL -> new ProfilInternational();
        };
    }
}
