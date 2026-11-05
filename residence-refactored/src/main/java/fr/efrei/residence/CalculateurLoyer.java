package fr.efrei.residence;

import java.util.Objects;
import java.util.Set;

public class CalculateurLoyer {

    public static final double COEFF_MEUBLE = DecorateurMeuble.COEFFICIENT;
    public static final double COEFF_REZ_DE_CHAUSSEE = DecorateurRezDeChaussee.COEFFICIENT;

    public static final double OPTION_PARKING = DecorateurParking.MONTANT;
    public static final double OPTION_LAVERIE = DecorateurLaverie.MONTANT;
    public static final double OPTION_FIBRE = DecorateurFibre.MONTANT;
    public static final double OPTION_ASSURANCE = DecorateurAssurance.MONTANT;

    private final AssembleurLoyer assembleur;

    public CalculateurLoyer(AssembleurLoyer assembleur) {
        this.assembleur = Objects.requireNonNull(assembleur, "assembleur");
    }

    /**
     * Loyer mensuel hors charges, options comprises, remise déduite.
     */
    public double calculerLoyer(Logement l, ProfilEtudiant profil, int echelon, Set<OptionLoyer> options) {
        double loyer = assembleur.assembler(l, options, profil, echelon).calculer();
        return Math.round(loyer * 100.0) / 100.0;
    }

    public double calculerCharges(Logement l) {
        return TarificationLogement.pour(l.getType()).charges();
    }
}
