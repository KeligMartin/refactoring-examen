package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuittanceGenerateurTest {

    private final QuittanceGenerateur generateur = new QuittanceGenerateur();

    @Test
    void genereLeTexteDeLaQuittancePourUnMoisPaye() {
        Etudiant etudiant = new Etudiant("Martin", "Alice", "alice@example.fr", "0600000000", null,
                ProfilEtudiant.STANDARD, 0, 1500, false, CanalNotification.EMAIL);
        Logement logement = new Studio("A001", 26, 1, true);
        Bail bail = new Bail("BAIL-001", etudiant, logement, LocalDate.of(2026, 8, 25),
                Set.of(OptionLoyer.PARKING, OptionLoyer.ASSURANCE));
        bail.setLoyerHorsCharges(451.67);
        bail.setCharges(35.0);
        bail.paiements.add(new Paiement("2026-09", LocalDate.of(2026, 9, 10), 400.0, 24.33, 511.0));

        String attendu = """
                ==========================================
                   RÉSIDENCE CAMPUS VERT - QUITTANCE
                ==========================================
                Bail      : BAIL-001
                Locataire : Alice MARTIN (STANDARD)
                Logement  : A001 - STUDIO 26.0 m² meublé
                Période   : 2026-09
                Options   : parking, assurance
                ------------------------------------------
                Loyer HC  : 451,67 €
                Charges   : 35,00 €
                Pénalité  : 24,33 €
                Total dû  : 511,00 €
                Payé le   : 10/09/2026 : 400,00 €
                RESTE DÛ  : 111,00 €
                ==========================================""";

        assertEquals(attendu, generateur.generer(bail, "2026-09"));
    }

    @Test
    void retourneUnMessageSiAucunPaiementNExistePourLeMois() {
        Etudiant etudiant = new Etudiant("Martin", "Alice", "alice@example.fr", "0600000000", null,
                ProfilEtudiant.STANDARD, 0, 1500, false, CanalNotification.EMAIL);
        Bail bail = new Bail("BAIL-001", etudiant, new Studio("A001", 26, 1, true),
                LocalDate.of(2026, 8, 25), Set.of());

        assertEquals("Aucun paiement pour 2026-10", generateur.generer(bail, "2026-10"));
    }
}
