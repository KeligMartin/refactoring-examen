package fr.efrei.residence;

import java.util.Objects;
import java.util.Set;

public class AssembleurLoyer {

    public Loyer assembler(Logement logement, Set<OptionLoyer> options, ProfilEtudiant profil, int echelon) {
        Objects.requireNonNull(logement, "logement");
        Objects.requireNonNull(options, "options");
        Objects.requireNonNull(profil, "profil");

        TarificationLogement tarification = TarificationLogement.pour(logement.getType());
        Loyer loyer = new LoyerBase(tarification.loyerDeBase(logement));

        if (logement.isMeuble()) {
            loyer = new DecorateurMeuble(loyer);
        }
        if (logement.getEtage() == Logement.ETAGE_REZ_DE_CHAUSSEE) {
            loyer = new DecorateurRezDeChaussee(loyer);
        }
        if (options.contains(OptionLoyer.MENAGE)) {
            loyer = new DecorateurMenage(loyer, tarification);
        }
        if (options.contains(OptionLoyer.PARKING)) {
            loyer = new DecorateurParking(loyer);
        }
        if (options.contains(OptionLoyer.LAVERIE)) {
            loyer = new DecorateurLaverie(loyer);
        }
        if (options.contains(OptionLoyer.FIBRE)) {
            loyer = new DecorateurFibre(loyer, tarification);
        }
        if (options.contains(OptionLoyer.ASSURANCE)) {
            loyer = new DecorateurAssurance(loyer);
        }

        return new DecorateurRemise(loyer, PolitiqueProfil.pour(profil), echelon);
    }
}
