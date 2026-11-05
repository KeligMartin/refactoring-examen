package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecorateurLoyerTest {

    @Test
    void loyerBaseRetourneSonMontant() {
        Loyer loyer = new LoyerBase(500.0);

        assertEquals(500.0, loyer.calculer());
    }

    @Test
    void decorateurDelegueLeCalculAuLoyerDecore() {
        Loyer loyer = new DecorateurLoyer(new LoyerBase(500.0)) {};

        assertEquals(500.0, loyer.calculer());
    }
}