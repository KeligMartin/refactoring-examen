package fr.efrei.residence;

public class CalculateurLoyer {

    public static final double TARIF_STUDIO_M2 = 14;
    public static final double LOYER_STUDIO_MINIMUM = 350;
    public static final double TARIF_T1_M2 = 13;
    public static final double FORFAIT_T1 = 50;
    public static final double TARIF_T2_M2 = 12;
    public static final double FORFAIT_T2 = 100;
    public static final double TARIF_COLOCATION_M2 = 11;

    public static final double COEFF_MEUBLE = 1.15;
    public static final double COEFF_REZ_DE_CHAUSSEE = 0.95;

    public static final double COEFF_MENAGE = 1.08;
    public static final double COEFF_MENAGE_COLOCATION = 1.04;
    public static final double OPTION_PARKING = 45;
    public static final double OPTION_LAVERIE = 12;
    public static final double OPTION_FIBRE = 20;
    public static final double OPTION_ASSURANCE = 9;

    public static final double REMISE_BOURSIER_PAR_ECHELON = 0.05;
    public static final double REMISE_BOURSIER_MAXIMUM = 0.30;
    public static final double COEFF_REMISE_ALTERNANT = 0.90;

    public static final double CHARGES_FORFAIT = 35;
    public static final double CHARGES_FORFAIT_COLOCATION = 25;

    /**
     * Loyer mensuel hors charges, options comprises, remise déduite.
     */
    public double calculerLoyer(Logement l, ProfilEtudiant profil, int echelon, boolean parking, boolean menage,
                                boolean laverie, boolean fibre, boolean assurance) {
        // loyer de base selon le type
        double loyer;
        switch (l.getType()) {
            case STUDIO:
                loyer = l.getSurface() * TARIF_STUDIO_M2;
                if (loyer < LOYER_STUDIO_MINIMUM) loyer = LOYER_STUDIO_MINIMUM;
                break;
            case T1:
                loyer = l.getSurface() * TARIF_T1_M2 + FORFAIT_T1;
                break;
            case T2:
                loyer = l.getSurface() * TARIF_T2_M2 + FORFAIT_T2;
                break;
            case COLOCATION:
                loyer = l.getSurface() * TARIF_COLOCATION_M2 / l.getNombrePlaces();
                break;
            default:
                throw new IllegalArgumentException("Type de logement inconnu : " + l.getType());
        }

        if (l.isMeuble()) loyer = loyer * COEFF_MEUBLE;
        if (l.getEtage() == Logement.ETAGE_REZ_DE_CHAUSSEE) loyer = loyer * COEFF_REZ_DE_CHAUSSEE;

        // options
        if (menage) {
            if (l.getType() == TypeLogement.COLOCATION) {
                loyer = loyer * COEFF_MENAGE_COLOCATION; // parties communes déjà nettoyées
            } else {
                loyer = loyer * COEFF_MENAGE;
            }
        }
        if (parking) loyer = loyer + OPTION_PARKING;
        if (laverie) loyer = loyer + OPTION_LAVERIE;
        if (fibre && l.getType() != TypeLogement.COLOCATION) loyer = loyer + OPTION_FIBRE; // fibre incluse en coloc
        if (assurance) loyer = loyer + OPTION_ASSURANCE;

        // remise selon le profil
        if (profil == ProfilEtudiant.BOURSIER) {
            double taux = echelon * REMISE_BOURSIER_PAR_ECHELON;
            if (taux > REMISE_BOURSIER_MAXIMUM) taux = REMISE_BOURSIER_MAXIMUM;
            loyer = loyer * (1 - taux);
        } else if (profil == ProfilEtudiant.ALTERNANT) {
            loyer = loyer * COEFF_REMISE_ALTERNANT;
        }

        return Math.round(loyer * 100.0) / 100.0;
    }

    public double calculerCharges(Logement l) {
        if (l.getType() == TypeLogement.COLOCATION) return CHARGES_FORFAIT_COLOCATION;
        return CHARGES_FORFAIT;
    }
}
