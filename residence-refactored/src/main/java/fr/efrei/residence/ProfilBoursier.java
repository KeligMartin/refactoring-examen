package fr.efrei.residence;

public class ProfilBoursier implements PolitiqueProfil {
    public static final double REMISE_PAR_ECHELON = 0.05;
    public static final double REMISE_MAXIMUM = 0.30;
    public static final int ECHELON_MINIMUM = 0;
    public static final int ECHELON_MAXIMUM = 7;

    @Override
    public double appliquerRemise(double loyer, int echelon) {
        double taux = echelon * REMISE_PAR_ECHELON;
        if (taux > REMISE_MAXIMUM) taux = REMISE_MAXIMUM;
        return loyer * (1 - taux);
    }
    @Override
    public boolean estSolvable(double ressources, double loyerCc) {
        return true; // garantie Visale
    }
    @Override
    public double depotGarantie(double loyerHc) {
        return 0;
    }
    @Override
    public double fraisDossier() {
        return 0;
    }
    @Override
    public int dureePreavisJours() {
        return PREAVIS_JOURS_COURT;
    }
    @Override
    public double penaliteSiRetard(double loyerCc) {
        return 0;
    }
    @Override
    public String motifRefusInscription(int echelon, String telephone) {
        if (echelon < ECHELON_MINIMUM || echelon > ECHELON_MAXIMUM) {
            return "Inscription refusée : échelon de bourse invalide";
        }
        return null;
    }
    @Override
    public boolean recoitEmailComplementaire(CanalNotification canal) {
        return false;
    }
}
