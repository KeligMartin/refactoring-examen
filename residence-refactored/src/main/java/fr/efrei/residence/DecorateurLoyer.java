package fr.efrei.residence;

import java.util.Objects;

public abstract class DecorateurLoyer implements Loyer {

    protected final Loyer loyer;

    protected DecorateurLoyer(Loyer loyer) {
        this.loyer = Objects.requireNonNull(loyer, "loyer");
    }

    @Override
    public double calculer() {
        return loyer.calculer();
    }
}