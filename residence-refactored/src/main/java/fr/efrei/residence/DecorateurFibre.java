package fr.efrei.residence;

import java.util.Objects;

public class DecorateurFibre extends DecorateurLoyer {

    public static final double MONTANT = 20;

    private final TarificationLogement tarification;

    public DecorateurFibre(Loyer loyer, TarificationLogement tarification) {
        super(loyer);
        this.tarification = Objects.requireNonNull(tarification, "tarification");
    }

    @Override
    public double calculer() {
        return tarification.fibrePayante() ? loyer.calculer() + MONTANT : loyer.calculer();
    }
}
