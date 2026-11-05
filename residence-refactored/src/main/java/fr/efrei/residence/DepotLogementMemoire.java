package fr.efrei.residence;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DepotLogementMemoire implements LogementRepository {

    private final Map<String, Logement> logementsParNumero = new LinkedHashMap<>();

    @Override
    public void sauvegarder(Logement logement) {
        logementsParNumero.put(logement.getNumero(), logement);
    }

    @Override
    public Logement trouverParNumero(String numero) {
        return logementsParNumero.get(numero);
    }

    @Override
    public List<Logement> tous() {
        return new ArrayList<>(logementsParNumero.values());
    }
}
