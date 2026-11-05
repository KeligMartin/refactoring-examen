package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class DepotMemoireTest {

    @Test
    void depotEtudiantPermetRechercheParEmailEtParIdentifiant() {
        EtudiantRepository depot = new DepotEtudiantMemoire();
        Etudiant etudiant = creerEtudiant();

        depot.sauvegarder(etudiant);

        assertSame(etudiant, depot.trouverParEmail(etudiant.getEmail()));
        assertSame(etudiant, depot.trouverParId(etudiant.getId()));
        assertEquals(1, depot.tous().size());
    }

    @Test
    void depotBailPermetSauvegardeMiseAJourEtRecherche() {
        BailRepository depot = new DepotBailMemoire();
        Bail bail = new Bail("BAIL-001", creerEtudiant(), new Studio("A001", 20, 1, false),
                LocalDate.of(2026, 8, 25), Set.of());

        depot.sauvegarder(bail);
        bail.setStatut(StatutBail.ACTIF);
        depot.mettreAJour(bail);

        assertSame(bail, depot.trouverParId("BAIL-001"));
        assertEquals(StatutBail.ACTIF, depot.trouverParId("BAIL-001").getStatut());
        assertEquals(1, depot.tous().size());
    }

    @Test
    void depotLogementRechercheEtRetourneLeParc() {
        LogementRepository depot = new DepotLogementMemoire();
        Logement logement = new Studio("A001", 20, 1, false);

        depot.sauvegarder(logement);

        assertSame(logement, depot.trouverParNumero("A001"));
        assertNull(depot.trouverParNumero("A999"));
        assertEquals(1, depot.tous().size());
    }

    private Etudiant creerEtudiant() {
        return new Etudiant("ETU-001", "Martin", "Alice", "alice@example.fr", "0600000000", null,
                ProfilEtudiant.STANDARD, 0, 1500, false, CanalNotification.EMAIL);
    }
}
