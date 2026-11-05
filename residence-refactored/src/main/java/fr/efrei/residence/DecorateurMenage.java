package fr.efrei.residence;

import java.util.Objects;

public class DecorateurMenage extends DecorateurLoyer {

    private final TarificationLogement tarification;

    public DecorateurMenage(Loyer loyer, TarificationLogement tarification) {
        super(loyer);
        this.tarification = Objects.requireNonNull(tarification, "tarification");
    }

    @Override
    public double calculer() {
        return loyer.calculer() * tarification.coefficientMenage();
    }
}
