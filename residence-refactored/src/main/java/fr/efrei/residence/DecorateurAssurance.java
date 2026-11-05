package fr.efrei.residence;

public class DecorateurAssurance extends DecorateurLoyer {

    public static final double MONTANT = 9;

    public DecorateurAssurance(Loyer loyer) {
        super(loyer);
    }

    @Override
    public double calculer() {
        return loyer.calculer() + MONTANT;
    }
}
