package fr.efrei.residence;

import java.util.Arrays;
import java.util.stream.Collectors;

public class QuittanceGenerateur {

    /**
     * Génère la quittance d'un mois payé (format AAAA-MM).
     */
    public String generer(Bail bail, String mois) {
        Paiement paiement = bail.getPaiements().stream()
                .filter(x -> x.mois.equals(mois))
                .findFirst()
                .orElse(null);
        if (paiement == null) {
            return "Aucun paiement pour " + mois;
        }

        StringBuilder quittance = new StringBuilder();
        quittance.append("==========================================\n");
        quittance.append("   RÉSIDENCE CAMPUS VERT - QUITTANCE\n");
        quittance.append("==========================================\n");
        quittance.append("Bail      : ").append(bail.getId()).append('\n');
        quittance.append("Locataire : ").append(bail.getEtudiant().getNomComplet())
                .append(" (").append(bail.getEtudiant().getProfil()).append(")\n");
        quittance.append("Logement  : ").append(bail.getLogement().getNumero()).append(" - ")
                .append(bail.getLogement().getType()).append(' ').append(bail.getLogement().getSurface()).append(" m²");
        if (bail.getLogement().isMeuble()) {
            quittance.append(" meublé");
        }
        quittance.append('\n');
        quittance.append("Période   : ").append(mois).append('\n');
        quittance.append("Options   : ").append(listerOptions(bail)).append('\n');
        quittance.append("------------------------------------------\n");
        quittance.append("Loyer HC  : ").append(Utils.euros(bail.getLoyerHorsCharges())).append('\n');
        quittance.append("Charges   : ").append(Utils.euros(bail.getCharges())).append('\n');
        if (paiement.penalite > 0) {
            quittance.append("Pénalité  : ").append(Utils.euros(paiement.penalite)).append('\n');
        }
        quittance.append("Total dû  : ").append(Utils.euros(paiement.du)).append('\n');
        quittance.append("Payé le   : ").append(Utils.date(paiement.date)).append(" : ")
                .append(Utils.euros(paiement.montant)).append('\n');
        if (paiement.montant < paiement.du) {
            quittance.append("RESTE DÛ  : ").append(Utils.euros(Utils.arrondir(paiement.du - paiement.montant))).append('\n');
        }
        quittance.append("==========================================");
        return quittance.toString();
    }

    private String listerOptions(Bail bail) {
        String resultat = Arrays.stream(OptionLoyer.values())
                .filter(bail.getOptions()::contains)
                .map(OptionLoyer::getLibelle)
                .collect(Collectors.joining(", "));
        return resultat.isEmpty() ? "aucune" : resultat;
    }
}
