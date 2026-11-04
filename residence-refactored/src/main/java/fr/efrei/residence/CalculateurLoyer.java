package fr.efrei.residence;

public class CalculateurLoyer {

    /**
     * Loyer mensuel hors charges, options comprises, remise déduite.
     */
    public double calculerLoyer(Logement l, String profil, int echelon, boolean parking, boolean menage,
                                boolean laverie, boolean fibre, boolean assurance) {
        // loyer de base selon le type
        TarificationLogement tarif = TarificationLogement.pour(l.getType());
        double loyer = tarif.loyerDeBase(l);

        if (l.isMeuble()) loyer = loyer * 1.15;
        if (l.getEtage() == 0) loyer = loyer * 0.95;

        // options
        if (menage) loyer = loyer * tarif.coefficientMenage();

        if (parking) loyer = loyer + 45;
        if (laverie) loyer = loyer + 12;
        if (fibre && tarif.fibrePayante()) loyer = loyer + 20;
        if (assurance) loyer = loyer + 9;

        loyer = PolitiqueProfil.pour(profil).appliquerRemise(loyer, echelon);

        return Math.round(loyer * 100.0) / 100.0;

    }
    public double calculerCharges(Logement l) {
        return TarificationLogement.pour(l.getType()).charges();
    }
}
