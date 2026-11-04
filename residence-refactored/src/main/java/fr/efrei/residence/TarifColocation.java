package fr.efrei.residence;

public class TarifColocation implements TarificationLogement {
    @Override
    public double loyerDeBase(Logement l) {
        return l.getSurface() * 11 / l.getNombrePlaces();
    }
    @Override
    public double coefficientMenage() {
        return 1.04; 
    }
    @Override
    public boolean fibrePayante() {
        return false; 
    }
    @Override
    public double charges() {
        return 25;
    }
}
