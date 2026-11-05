package fr.efrei.residence;

import java.util.List;

public interface LogementRepository {

    void sauvegarder(Logement logement);

    Logement trouverParNumero(String numero);

    List<Logement> tous();
}
