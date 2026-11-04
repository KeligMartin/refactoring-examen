package fr.efrei.residence;

public class TarifT1 implements TarificationLogement {
    public static final double TARIF_M2 = 13;
    public static final double FORFAIT = 50;

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
