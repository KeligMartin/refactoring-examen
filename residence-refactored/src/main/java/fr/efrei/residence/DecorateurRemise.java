package fr.efrei.residence;

import java.util.Objects;

public class DecorateurRemise extends DecorateurLoyer {

    private final PolitiqueProfil politique;
    private final int echelon;

    public DecorateurRemise(Loyer loyer, PolitiqueProfil politique, int echelon) {
        super(loyer);
        this.politique = Objects.requireNonNull(politique, "politique");
        this.echelon = echelon;
    }

    @Override
    public double calculer() {
        return politique.appliquerRemise(loyer.calculer(), echelon);
    }
}
