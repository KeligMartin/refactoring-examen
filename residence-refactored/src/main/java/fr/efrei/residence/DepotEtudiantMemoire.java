package fr.efrei.residence;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DepotEtudiantMemoire implements EtudiantRepository {

    private final Map<String, Etudiant> etudiantsParEmail = new LinkedHashMap<>();

    @Override
    public void sauvegarder(Etudiant etudiant) {
        etudiantsParEmail.put(etudiant.getEmail(), etudiant);
    }

    @Override
    public Etudiant trouverParEmail(String email) {
        return etudiantsParEmail.get(email);
    }

    @Override
    public Etudiant trouverParId(String id) {
        return etudiantsParEmail.values().stream()
                .filter(etudiant -> etudiant.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Etudiant> tous() {
        return new ArrayList<>(etudiantsParEmail.values());
    }
}
