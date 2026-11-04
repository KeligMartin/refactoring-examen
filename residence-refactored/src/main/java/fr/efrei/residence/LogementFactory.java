package fr.efrei.residence;

public class LogementFactory {

    public static Logement creer(TypeLogement type, String numero, double surface, int etage, boolean meuble, int param) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case STUDIO:
                return new Studio(numero, surface, etage, meuble);
            case T1:
                return new T1(numero, surface, etage, meuble);
            case T2:
                return new T2(numero, surface, etage, meuble, param);
            case COLOCATION:
                int places = param < Colocation.PLACES_MINIMUM ? Colocation.PLACES_MINIMUM : param;
                return new Colocation(numero, surface, etage, meuble, places);
            default:
                return null;
        }
    }
}
