package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecorateurOptionsTest {

    private static final double DELTA = 0.000001;

    @Test
    void menageAppliqueLeCoefficientDuTypeDeLogement() {
        Loyer loyer = new DecorateurMenage(new LoyerBase(100.0), new TarifStudio());

        assertEquals(108.0, loyer.calculer(), DELTA);
    }

    @Test
    void menageUtiliseLeCoefficientSpecifiqueALaColocation() {
        Loyer loyer = new DecorateurMenage(new LoyerBase(100.0), new TarifColocation());

        assertEquals(104.0, loyer.calculer(), DELTA);
    }

    @Test
    void parkingAjouteSonMontantForfaitaire() {
        Loyer loyer = new DecorateurParking(new LoyerBase(100.0));

        assertEquals(145.0, loyer.calculer(), DELTA);
    }

    @Test
    void laverieAjouteSonMontantForfaitaire() {
        Loyer loyer = new DecorateurLaverie(new LoyerBase(100.0));

        assertEquals(112.0, loyer.calculer(), DELTA);
    }

    @Test
    void fibreAjouteSonMontantQuandElleEstPayante() {
        Loyer loyer = new DecorateurFibre(new LoyerBase(100.0), new TarifStudio());

        assertEquals(120.0, loyer.calculer(), DELTA);
    }

    @Test
    void fibreIncluseNaugmentePasLeLoyer() {
        Loyer loyer = new DecorateurFibre(new LoyerBase(100.0), new TarifColocation());

        assertEquals(100.0, loyer.calculer(), DELTA);
    }

    @Test
    void assuranceAjouteSonMontantForfaitaire() {
        Loyer loyer = new DecorateurAssurance(new LoyerBase(100.0));

        assertEquals(109.0, loyer.calculer(), DELTA);
    }
}
