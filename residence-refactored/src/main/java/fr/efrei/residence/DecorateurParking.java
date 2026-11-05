package fr.efrei.residence;

public class DecorateurParking extends DecorateurLoyer {

    public static final double MONTANT = 45;

    public DecorateurParking(Loyer loyer) {
        super(loyer);
    }

    @Override
    public double calculer() {
        return loyer.calculer() + MONTANT;
    }
}
