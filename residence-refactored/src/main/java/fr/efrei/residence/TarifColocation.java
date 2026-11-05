package fr.efrei.residence;

public class TarifColocation implements TarificationLogement {
    public static final double TARIF_M2 = 11;
    public static final double COEFF_MENAGE_COLOCATION = 1.04;
    public static final double CHARGES_FORFAIT_COLOCATION = 25;

    @Override
    public double loyerDeBase(Logement l) {
        return l.getSurface() * TARIF_M2 / l.getNombrePlaces();
    }
    @Override
    public double coefficientMenage() {
        return COEFF_MENAGE_COLOCATION; // parties communes déjà nettoyées
    }
    @Override
    public boolean fibrePayante() {
        return false; // fibre incluse en coloc
    }
    @Override
    public double charges() {
        return CHARGES_FORFAIT_COLOCATION;
    }
}
