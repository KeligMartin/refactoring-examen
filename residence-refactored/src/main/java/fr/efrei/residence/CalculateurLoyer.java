package fr.efrei.residence;

public class CalculateurLoyer {

    public static final double COEFF_MEUBLE = 1.15;
    public static final double COEFF_REZ_DE_CHAUSSEE = 0.95;

    public static final double OPTION_PARKING = DecorateurParking.MONTANT;
    public static final double OPTION_LAVERIE = DecorateurLaverie.MONTANT;
    public static final double OPTION_FIBRE = DecorateurFibre.MONTANT;
    public static final double OPTION_ASSURANCE = DecorateurAssurance.MONTANT;

    /**
     * Loyer mensuel hors charges, options comprises, remise déduite.
     */
    public double calculerLoyer(Logement l, ProfilEtudiant profil, int echelon, boolean parking, boolean menage,
                                boolean laverie, boolean fibre, boolean assurance) {
        TarificationLogement tarif = TarificationLogement.pour(l.getType());
        double loyer = tarif.loyerDeBase(l);

        if (l.isMeuble()) loyer = loyer * COEFF_MEUBLE;
        if (l.getEtage() == Logement.ETAGE_REZ_DE_CHAUSSEE) loyer = loyer * COEFF_REZ_DE_CHAUSSEE;

        // options
        if (menage) loyer = loyer * tarif.coefficientMenage();
        if (parking) loyer = loyer + OPTION_PARKING;
        if (laverie) loyer = loyer + OPTION_LAVERIE;
        if (fibre && tarif.fibrePayante()) loyer = loyer + OPTION_FIBRE;
        if (assurance) loyer = loyer + OPTION_ASSURANCE;

        loyer = PolitiqueProfil.pour(profil).appliquerRemise(loyer, echelon);

        return Math.round(loyer * 100.0) / 100.0;
    }

    public double calculerCharges(Logement l) {
        return TarificationLogement.pour(l.getType()).charges();
    }
}
