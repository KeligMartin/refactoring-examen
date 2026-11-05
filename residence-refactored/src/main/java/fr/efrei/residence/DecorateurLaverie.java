package fr.efrei.residence;

public class DecorateurLaverie extends DecorateurLoyer {

    public static final double MONTANT = 12;

    public DecorateurLaverie(Loyer loyer) {
        super(loyer);
    }

    @Override
    public double calculer() {
        return loyer.calculer() + MONTANT;
    }
}
