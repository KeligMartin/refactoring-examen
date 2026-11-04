package fr.efrei.residence;

public class TarifT1 implements TarificationLogement {
    @Override
    public double loyerDeBase(Logement l) {
        return l.getSurface() * 13 + 50;
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
