package fr.efrei.residence;

import java.util.List;

public interface EtudiantRepository {

    void sauvegarder(Etudiant etudiant);

    Etudiant trouverParEmail(String email);

    Etudiant trouverParId(String id);

    List<Etudiant> tous();
}
