package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServiceAttributionTest {

    private final ServiceAttribution service = new ServiceAttribution();
    private final CalculateurLoyer calculateur = new CalculateurLoyer();

    @Test
    void choisitLePremierLogementSolvable() {
        Etudiant etudiant = etudiant(1200, false);
        Studio tropCher = new Studio("A001", 100, 1, false);
        Studio abordable = new Studio("A002", 20, 1, false);

        ResultatAttribution resultat = service.attribuer(etudiant, TypeLogement.STUDIO, Set.of(),
                List.of(tropCher, abordable), calculateur);

        assertEquals("A002", resultat.logement().getNumero());
        assertEquals(350.0, resultat.loyerHorsCharges());
        assertEquals(35.0, resultat.charges());
        assertTrue(tropCher.estLibre());
        assertFalse(abordable.estLibre());
    }

    @Test
    void sauteLesLogementsInaccessiblesAuxPersonnesPmr() {
        Etudiant etudiant = etudiant(2000, true);
        Studio nonAccessible = new Studio("B001", 20, 1, false);
        Studio accessible = new Studio("A001", 20, 1, false);

        ResultatAttribution resultat = service.attribuer(etudiant, TypeLogement.STUDIO, Set.of(),
                List.of(nonAccessible, accessible), calculateur);

        assertEquals("A001", resultat.logement().getNumero());
        assertTrue(nonAccessible.estLibre());
        assertFalse(accessible.estLibre());
    }

    @Test
    void neRetournePasDeResultatSiAucunLogementNestSolvable() {
        Etudiant etudiant = etudiant(100, false);
        Studio logement = new Studio("A001", 100, 1, false);

        ResultatAttribution resultat = service.attribuer(etudiant, TypeLogement.STUDIO, Set.of(),
                List.of(logement), calculateur);

        assertNull(resultat);
        assertTrue(logement.estLibre());
    }

    private Etudiant etudiant(double ressources, boolean pmr) {
        return new Etudiant("Martin", "Alice", "alice@example.fr", "0600000000", null,
                ProfilEtudiant.STANDARD, 0, ressources, pmr, CanalNotification.EMAIL);
    }
}
