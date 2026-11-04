package fr.efrei.residence;

public interface TarificationLogement {
    double loyerDeBase(Logement l);
    double coefficientMenage();
    boolean fibrePayante();
    double charges();

    static TarificationLogement pour(String type) {
        return switch (type) {
            case "STUDIO" -> new TarifStudio();
            case "T1" -> new TarifT1();
            case "T2" -> new TarifT2();
            case "COLOCATION" -> new TarifColocation();
            default -> throw new IllegalArgumentException("Type de logement inconnu : " + type);
        };
    }
}
