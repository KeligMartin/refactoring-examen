package fr.efrei.residence;

public class ProfilAlternant implements PolitiqueProfil {
    @Override
    public double appliquerRemise(double loyer, int echelon) {
        return loyer * 0.90;
    }
    @Override
    public boolean estSolvable(double ressources, double loyerCc) {
        return ressources >= loyerCc * 2;
    }
    @Override
    public double depotGarantie(double loyerHc) {
        return loyerHc;
    }
    @Override
    public double fraisDossier() {
        return 100;
    }
    @Override
    public int dureePreavisJours() {
        return 15;
    }
    @Override
    public double penaliteSiRetard(double loyerCc) {
        double penalite = loyerCc * 0.05;
        if (penalite < 10) penalite = 10;
        return penalite;
    }
    @Override
    public String motifRefusInscription(int echelon, String telephone) {
        return null;
    }
    @Override
    public boolean recoitEmailComplementaire(String canal) {
        return false;
    }
}
