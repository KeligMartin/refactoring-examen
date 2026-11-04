package fr.efrei.residence;

public class TarifStudio implements TarificationLogement {
    @Override
    public double loyerDeBase(Logement l) {
        double loyer = l.getSurface() * 14;
        if (loyer < 350) loyer = 350;
        return loyer;
    }
    @Override
    public double coefficientMenage() {
        return 1.08;
    }
    @Override
    public boolean fibrePayante() {
        return true;
    }
    @Override
    public double charges() {
        return 35;
    }
}
