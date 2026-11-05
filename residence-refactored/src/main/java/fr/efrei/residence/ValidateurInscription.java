package fr.efrei.residence;

import java.util.Objects;
import java.util.function.Predicate;

public class ValidateurInscription {

    public String motifRefus(String nom, String email, String telephone, String adresse, ProfilEtudiant profil,
                             int echelonBourse, CanalNotification canalPrefere, Predicate<String> emailDejaInscrit) {
        Objects.requireNonNull(emailDejaInscrit, "emailDejaInscrit");

        if (nom == null || nom.trim().isEmpty()) {
            return "Inscription refusée : nom obligatoire";
        }
        if (!Utils.emailValide(email)) {
            return "Inscription refusée : email invalide (" + email + ")";
        }
        if (emailDejaInscrit.test(email)) {
            return "Inscription refusée : " + email + " déjà inscrit";
        }
        String motifProfil = PolitiqueProfil.pour(profil).motifRefusInscription(echelonBourse, telephone);
        if (motifProfil != null) {
            return motifProfil;
        }
        if (canalPrefere == CanalNotification.SMS && telephone == null) {
            return "Inscription refusée : téléphone obligatoire pour les SMS";
        }
        if (canalPrefere == CanalNotification.COURRIER && (adresse == null || adresse.isEmpty())) {
            return "Inscription refusée : adresse obligatoire pour le courrier";
        }
        return null;
    }
}
