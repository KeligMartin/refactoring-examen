package fr.efrei.residence;

public class TarifStudio implements TarificationLogement {
    public static final double TARIF_M2 = 14;
    public static final double LOYER_MINIMUM = 350;

    @Override
    public double loyerDeBase(Logement l) {
        double loyer = l.getSurface() * TARIF_M2;
        if (loyer < LOYER_MINIMUM) loyer = LOYER_MINIMUM;
        return loyer;
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
