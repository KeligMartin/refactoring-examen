package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ValidateurInscriptionTest {

    private final ValidateurInscription validateur = new ValidateurInscription();

    @Test
    void refuseUnNomManquant() {
        assertEquals("Inscription refusée : nom obligatoire",
                valider(null, "alice@example.fr", "0600000000", null, ProfilEtudiant.STANDARD, 0,
                        CanalNotification.EMAIL, email -> false));
    }

    @Test
    void refuseUnEmailInvalide() {
        assertEquals("Inscription refusée : email invalide (invalide)",
                valider("Martin", "invalide", "0600000000", null, ProfilEtudiant.STANDARD, 0,
                        CanalNotification.EMAIL, email -> false));
    }

    @Test
    void refuseUneAdresseEmailDejaInscrite() {
        assertEquals("Inscription refusée : alice@example.fr déjà inscrit",
                valider("Martin", "alice@example.fr", "0600000000", null, ProfilEtudiant.STANDARD, 0,
                        CanalNotification.EMAIL, email -> true));
    }

    @Test
    void refuseUnEchelonBoursierInvalide() {
        assertEquals("Inscription refusée : échelon de bourse invalide",
                valider("Martin", "alice@example.fr", "0600000000", null, ProfilEtudiant.BOURSIER, 8,
                        CanalNotification.EMAIL, email -> false));
    }

    @Test
    void refuseUnNumeroInternationalNonPrefixeParPlus() {
        assertEquals("Inscription refusée : numéro international (+XX) obligatoire",
                valider("Martin", "alice@example.fr", "0600000000", null, ProfilEtudiant.INTERNATIONAL, 0,
                        CanalNotification.EMAIL, email -> false));
    }

    @Test
    void refuseLesSmsSansTelephone() {
        assertEquals("Inscription refusée : téléphone obligatoire pour les SMS",
                valider("Martin", "alice@example.fr", null, null, ProfilEtudiant.STANDARD, 0,
                        CanalNotification.SMS, email -> false));
    }

    @Test
    void refuseLeCourrierSansAdresse() {
        assertEquals("Inscription refusée : adresse obligatoire pour le courrier",
                valider("Martin", "alice@example.fr", "0600000000", null, ProfilEtudiant.STANDARD, 0,
                        CanalNotification.COURRIER, email -> false));
    }

    @Test
    void accepteUneInscriptionValide() {
        assertNull(valider("Martin", "alice@example.fr", "0600000000", null, ProfilEtudiant.STANDARD, 0,
                CanalNotification.EMAIL, email -> false));
    }

    private String valider(String nom, String email, String telephone, String adresse, ProfilEtudiant profil,
                           int echelonBourse, CanalNotification canal, java.util.function.Predicate<String> emailExiste) {
        return validateur.motifRefus(nom, email, telephone, adresse, profil, echelonBourse, canal, emailExiste);
    }
}
