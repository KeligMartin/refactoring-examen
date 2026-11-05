package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RapportGenerateurTest {

    @Test
    void genereLeRapportInitialAvecLeFormatAttendu() {
        List<Logement> logements = List.of(
                new Studio("A001", 20, 0, false),
                new Studio("A002", 20, 1, false),
                new Studio("A003", 20, 2, false),
                new T1("B101", 30, 1, false),
                new T1("B102", 30, 2, false),
                new T2("C201", 50, 1, false, 3),
                new T2("C202", 50, 2, false, 3),
                new Colocation("D301", 80, 1, false, 7));

        String attendu = """
                =============== RAPPORT Résidence Campus Vert ===============
                Studios      : 0/3
                T1           : 0/2
                T2           : 0/2
                Colocation   : 0/7 places
                Occupation   : 0 %
                Balcons      : 6.0 m²
                Loyers CC    : 0,00 € / mois
                Cautions     : 0,00 €
                Impayés      : 0,00 €
                SMS envoyés  : 0
                Timbres      : 0,00 €
                ============================================================""";

        String rapport = new RapportGenerateur().generer(logements, List.of(), 0, 0);

        assertEquals(attendu, rapport);
    }
}
