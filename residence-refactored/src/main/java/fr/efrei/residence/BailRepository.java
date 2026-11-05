package fr.efrei.residence;

import java.util.List;

public interface BailRepository {

    void sauvegarder(Bail bail);

    void mettreAJour(Bail bail);

    Bail trouverParId(String id);

    List<Bail> tous();
}
