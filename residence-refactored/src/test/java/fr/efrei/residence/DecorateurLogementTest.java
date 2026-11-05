package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecorateurLogementTest {

    @Test
    void meubleAugmenteLeLoyerDe15Pourcent() {
        Loyer loyer = new DecorateurMeuble(new LoyerBase(100.0));

        assertEquals(115.0, loyer.calculer(), 0.000001);
    }

    @Test
    void rezDeChausseeDiminueLeLoyerDe5Pourcent() {
        Loyer loyer = new DecorateurRezDeChaussee(new LoyerBase(100.0));

        assertEquals(95.0, loyer.calculer(), 0.000001);
    }

    @Test
    void lesDecorateursPeuventEtreComposes() {
        Loyer loyer = new DecorateurRezDeChaussee(new DecorateurMeuble(new LoyerBase(100.0)));

        assertEquals(109.25, loyer.calculer(), 0.000001);
    }
}
