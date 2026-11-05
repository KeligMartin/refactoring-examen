package fr.efrei.residence;

public interface TarificationLogement {
    double COEFF_MENAGE = 1.08;
    double CHARGES_FORFAIT = 35;

    double loyerDeBase(Logement l);
    double coefficientMenage();
    boolean fibrePayante();
    double charges();

    static TarificationLogement pour(TypeLogement type) {
        return switch (type) {
            case STUDIO -> new TarifStudio();
            case T1 -> new TarifT1();
            case T2 -> new TarifT2();
            case COLOCATION -> new TarifColocation();
        };
    }
}
