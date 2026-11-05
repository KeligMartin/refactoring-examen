package fr.efrei.residence;

public class ProfilInternational implements PolitiqueProfil {
    public static final int CAUTION_MOIS = 2;
    public static final double FRAIS_DOSSIER = 150;

    @Override
    public double appliquerRemise(double loyer, int echelon) {
        return loyer;
    }
    @Override
    public boolean estSolvable(double ressources, double loyerCc) {
        return ressources >= loyerCc * SOLVABILITE_RATIO_DEFAUT;
    }
    @Override
    public double depotGarantie(double loyerHc) {
        return loyerHc * CAUTION_MOIS;
    }
    @Override
    public double fraisDossier() {
        return FRAIS_DOSSIER;
    }
    @Override
    public int dureePreavisJours() {
        return PREAVIS_JOURS_LONG;
    }
    @Override
    public double penaliteSiRetard(double loyerCc) {
        double penalite = loyerCc * TAUX_PENALITE_RETARD;
        if (penalite < PENALITE_MINIMUM) penalite = PENALITE_MINIMUM;
        return penalite;
    }
    @Override
    public String motifRefusInscription(int echelon, String telephone) {
        if (telephone == null || !telephone.startsWith("+")) {
            return "Inscription refusée : numéro international (+XX) obligatoire";
        }
        return null;
    }
    @Override
    public boolean recoitEmailComplementaire(CanalNotification canal) {
        return canal != CanalNotification.EMAIL;
    }
}
