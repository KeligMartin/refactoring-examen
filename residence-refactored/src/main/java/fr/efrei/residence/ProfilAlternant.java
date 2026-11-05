package fr.efrei.residence;

public class ProfilAlternant implements PolitiqueProfil {
    public static final double COEFF_REMISE = 0.90;
    public static final int SOLVABILITE_RATIO = 2;

    @Override
    public double appliquerRemise(double loyer, int echelon) {
        return loyer * COEFF_REMISE;
    }
    @Override
    public boolean estSolvable(double ressources, double loyerCc) {
        return ressources >= loyerCc * SOLVABILITE_RATIO;
    }
    @Override
    public double depotGarantie(double loyerHc) {
        return loyerHc;
    }
    @Override
    public double fraisDossier() {
        return FRAIS_DOSSIER_DEFAUT;
    }
    @Override
    public int dureePreavisJours() {
        return PREAVIS_JOURS_COURT;
    }
    @Override
    public double penaliteSiRetard(double loyerCc) {
        double penalite = loyerCc * TAUX_PENALITE_RETARD;
        if (penalite < PENALITE_MINIMUM) penalite = PENALITE_MINIMUM;
        return penalite;
    }
    @Override
    public String motifRefusInscription(int echelon, String telephone) {
        return null;
    }
    @Override
    public boolean recoitEmailComplementaire(CanalNotification canal) {
        return false;
    }
}
