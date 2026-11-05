package fr.efrei.residence;

public class DecorateurRezDeChaussee extends DecorateurLoyer {

    public static final double COEFFICIENT = 0.95;

    public DecorateurRezDeChaussee(Loyer loyer) {
        super(loyer);
    }

    @Override
    public double calculer() {
        return loyer.calculer() * COEFFICIENT;
    }
}
