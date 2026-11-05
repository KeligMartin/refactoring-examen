package fr.efrei.residence;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DepotBailMemoire implements BailRepository {

    private final Map<String, Bail> bauxParId = new LinkedHashMap<>();

    @Override
    public void sauvegarder(Bail bail) {
        bauxParId.put(bail.getId(), bail);
    }

    @Override
    public void mettreAJour(Bail bail) {
        bauxParId.put(bail.getId(), bail);
    }

    @Override
    public Bail trouverParId(String id) {
        return bauxParId.get(id);
    }

    @Override
    public List<Bail> tous() {
        List<Bail> baux = new ArrayList<>(bauxParId.values());
        baux.sort((a, b) -> a.getId().compareTo(b.getId()));
        return baux;
    }
}
