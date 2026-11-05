package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculateurLoyerTest {

    @Test
    void assembleLesCouchesDansLOrdreEtAppliqueLaRemiseEnDernier() {
        Logement logement = new Studio("A001", 20, Logement.ETAGE_REZ_DE_CHAUSSEE, true);
        EnumSet<OptionLoyer> options = EnumSet.allOf(OptionLoyer.class);

        double loyer = new CalculateurLoyer(new AssembleurLoyer())
                .calculerLoyer(logement, ProfilEtudiant.BOURSIER, 2, options);

        assertEquals(449.07, loyer);
    }

    @Test
    void calculerLoyerSansOptionsConserveLeLoyerDeBaseEtLesCouchesDuLogement() {
        Logement logement = new Studio("A001", 20, Logement.ETAGE_REZ_DE_CHAUSSEE, true);

        double loyer = new CalculateurLoyer(new AssembleurLoyer())
                .calculerLoyer(logement, ProfilEtudiant.STANDARD, 0, EnumSet.noneOf(OptionLoyer.class));

        assertEquals(382.37, loyer);
    }
}
