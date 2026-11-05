package fr.efrei.residence;

public class DecorateurMeuble extends DecorateurLoyer {

    public static final double COEFFICIENT = 1.15;

    public DecorateurMeuble(Loyer loyer) {
        super(loyer);
    }

    @Override
    public double calculer() {
        return loyer.calculer() * COEFFICIENT;
    }
}
