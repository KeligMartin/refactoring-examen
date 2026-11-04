package fr.efrei.residence;

public class ProfilInternational implements PolitiqueProfil {
    @Override
    public double appliquerRemise(double loyer, int echelon) {
        return loyer;
    }
    @Override
    public boolean estSolvable(double ressources, double loyerCc) {
        return ressources >= loyerCc * 3;
    }
    @Override
    public double depotGarantie(double loyerHc) {
        return loyerHc * 2;
    }
    @Override
    public double fraisDossier() {
        return 150;
    }
    @Override
    public int dureePreavisJours() {
        return 30;
    }
    @Override
    public double penaliteSiRetard(double loyerCc) {
        double penalite = loyerCc * 0.05;
        if (penalite < 10) penalite = 10;
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
    public boolean recoitEmailComplementaire(String canal) {
        return !canal.equals("EMAIL");
    }
}