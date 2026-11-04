package fr.efrei.residence;

public class TarifT2 implements TarificationLogement {
    public static final double TARIF_M2 = 12;
    public static final double FORFAIT = 100;

    @Override
    public double loyerDeBase(Logement l) {
        return l.getSurface() * TARIF_M2 + FORFAIT;
    }
    @Override
    public double coefficientMenage() {
        return COEFF_MENAGE;
    }
    @Override
    public boolean fibrePayante() {
        return true;
    }
    @Override
    public double charges() {
        return CHARGES_FORFAIT;
    }
}
