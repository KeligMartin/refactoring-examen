package fr.efrei.residence;

public class ProfilBoursier implements PolitiqueProfil {
    @Override
    public double appliquerRemise(double loyer, int echelon) {
        double taux = echelon * 0.05;
        if (taux > 0.30) taux = 0.30;
        return loyer * (1 - taux);
    }
    @Override
    public boolean estSolvable(double ressources, double loyerCc) {
        return true;
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
        return 15;
    }
    @Override
    public double penaliteSiRetard(double loyerCc) {
        return 0;
    }
    @Override
    public String motifRefusInscription(int echelon, String telephone) {
        if (echelon < 0 || echelon > 7) {
            return "Inscription refusée : échelon de bourse invalide";
        }
        return null;
    }
    @Override
    public boolean recoitEmailComplementaire(String canal) {
        return false;
    }
}
