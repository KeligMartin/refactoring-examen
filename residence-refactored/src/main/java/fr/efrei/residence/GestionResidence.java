package fr.efrei.residence;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Service principal de la résidence : inscriptions, attributions, baux, loyers, préavis, sorties.
 */
public class GestionResidence {

    public static int compteurBail = 0;

    private Database db = Database.getInstance();
    private CalculateurLoyer calculateur = new CalculateurLoyer();
    private final NotificationService notificationService;
    private final ValidateurInscription validateurInscription;
    private final ServiceAttribution serviceAttribution;

    public GestionResidence(NotificationService notificationService, ValidateurInscription validateurInscription,
                            ServiceAttribution serviceAttribution) {
        this.notificationService = Objects.requireNonNull(notificationService, "notificationService");
        this.validateurInscription = Objects.requireNonNull(validateurInscription, "validateurInscription");
        this.serviceAttribution = Objects.requireNonNull(serviceAttribution, "serviceAttribution");
    }

    // ------------------------------------------------------------------ étudiants

    public Etudiant inscrireEtudiant(String nom, String prenom, String email, String telephone, String adresse,
                                     ProfilEtudiant profil, int echelonBourse, double ressources, boolean pmr,
                                     CanalNotification canalPrefere) {
        String motif = validateurInscription.motifRefus(nom, email, telephone, adresse, profil, echelonBourse,
                canalPrefere, adresseEmail -> db.trouverEtudiantParEmail(adresseEmail) != null);
        if (motif != null) {
            System.out.println(motif);
            return null;
        }
        Etudiant e = new Etudiant(nom, prenom, email, telephone, adresse, profil, echelonBourse, ressources, pmr,
                canalPrefere);
        db.sauvegarderEtudiant(e);
        System.out.println("Étudiant inscrit : " + e.getId() + " " + e.getNomComplet() + " (" + profil + ")");
        return e;
    }

    // ------------------------------------------------------------------ logements

    public Logement ajouterLogement(TypeLogement type, String numero, double surface, int etage, boolean meuble, int param) {
        if (Residence.trouver(numero) != null) {
            System.out.println("Logement " + numero + " existe déjà");
            return null;
        }
        Logement l = LogementFactory.creer(type, numero, surface, etage, meuble, param);
        if (l == null) {
            System.out.println("Type de logement inconnu : " + type);
            return null;
        }
        Residence.logements.add(l);
        System.out.println("Logement ajouté : " + numero + " (" + type + ")");
        return l;
    }

    // ------------------------------------------------------------------ attribution

    public Bail attribuerLogement(Etudiant e, TypeLogement typeSouhaite, Set<OptionLoyer> options, LocalDate dateSignature) {
        ResultatAttribution resultat = serviceAttribution.attribuer(e, typeSouhaite, options, Residence.logements,
                calculateur);
        if (resultat == null) {
            return null;
        }
        Logement choisi = resultat.logement();

        compteurBail++;
        String id = "BAIL-" + String.format("%03d", compteurBail);
        Bail b = new Bail(id, e, choisi, dateSignature, options);
        b.setLoyerHorsCharges(resultat.loyerHorsCharges());
        b.setCharges(resultat.charges());

        PolitiqueProfil politique = PolitiqueProfil.pour(e.getProfil());
        b.setCaution(Utils.arrondir(politique.depotGarantie(resultat.loyerHorsCharges())));
        b.setFraisDossier(politique.fraisDossier());

        db.sauvegarderBail(b);
        System.out.println("  Bail " + id + " signé : " + choisi.getNumero() + ", " + Utils.euros(b.getLoyerChargesComprises())
                + " CC/mois, caution " + Utils.euros(b.getCaution()) + ", frais de dossier " + Utils.euros(b.getFraisDossier()));
        notificationService.notifier(e, "Votre bail " + id,
                "Bienvenue à la " + Residence.NOM + " ! Logement " + choisi.getNumero() + ", loyer "
                        + Utils.euros(b.getLoyerChargesComprises()) + " charges comprises.");
        return b;
    }

    // ------------------------------------------------------------------ cycle de vie du bail

    public boolean entrerDansLesLieux(String idBail, LocalDate date) {
        Bail b = db.trouverBail(idBail);
        if (b == null || b.getStatut() != StatutBail.SIGNE) {
            System.out.println("Entrée impossible pour " + idBail);
            return false;
        }
        if (date.isBefore(b.getDateSignature())) {
            System.out.println("Entrée impossible avant la signature du bail");
            return false;
        }
        b.setDateEntree(date);
        b.setStatut(StatutBail.ACTIF);
        db.mettreAJourBail(b);
        System.out.println("Entrée dans les lieux : " + idBail + " le " + Utils.date(date));
        notificationService.notifier(b.getEtudiant(), "État des lieux d'entrée",
                "Votre état des lieux d'entrée du logement " + b.getLogement().getNumero() + " est fait le "
                        + Utils.date(date) + ". Remise des clés effectuée. Pensez à souscrire une assurance habitation si ce n'est pas déjà fait.");
        return true;
    }

    public static final int JOUR_ECHEANCE_LOYER = 5;

    public boolean payerLoyer(String idBail, String mois, LocalDate datePaiement, double montant) {
        Bail b = db.trouverBail(idBail);
        if (b == null || !(b.getStatut() == StatutBail.ACTIF || b.getStatut() == StatutBail.PREAVIS)) {
            System.out.println("Paiement refusé pour " + idBail);
            return false;
        }
        LocalDate echeance = YearMonth.parse(mois).atDay(JOUR_ECHEANCE_LOYER);
        double penalite = 0;
        if (datePaiement.isAfter(echeance)) {
            PolitiqueProfil politique = PolitiqueProfil.pour(b.getEtudiant().getProfil());
            penalite = Utils.arrondir(politique.penaliteSiRetard(b.getLoyerChargesComprises()));
        }
        double du = Utils.arrondir(b.getLoyerChargesComprises() + penalite);
        b.paiements.add(new Paiement(mois, datePaiement, montant, penalite, du));
        b.soldeDu = Utils.arrondir(b.soldeDu + du - montant);
        db.mettreAJourBail(b);

        System.out.println("Paiement " + idBail + " " + mois + " : " + Utils.euros(montant) + " / " + Utils.euros(du)
                + (penalite > 0 ? " (dont pénalité " + Utils.euros(penalite) + ")" : ""));
        if (montant < du) {
            notificationService.notifier(b.getEtudiant(), "Paiement incomplet " + mois,
                    "Nous avons reçu " + Utils.euros(montant) + " sur " + Utils.euros(du) + ". Reste dû : "
                            + Utils.euros(Utils.arrondir(du - montant)) + ".");
        } else {
            notificationService.notifier(b.getEtudiant(), "Quittance " + mois, "Merci, votre loyer de " + mois + " est réglé.");
        }
        return true;
    }

    public boolean donnerPreavis(String idBail, LocalDate date) {
        Bail b = db.trouverBail(idBail);
        if (b == null || b.getStatut() != StatutBail.ACTIF) {
            System.out.println("Préavis impossible pour " + idBail);
            return false;
        }
        int jours = PolitiqueProfil.pour(b.getEtudiant().getProfil()).dureePreavisJours();
        b.setDateFin(date.plusDays(jours));
        b.setStatut(StatutBail.PREAVIS);
        db.mettreAJourBail(b);
        System.out.println("Préavis " + idBail + " : fin du bail le " + Utils.date(b.getDateFin()));
        notificationService.notifier(b.getEtudiant(), "Préavis de départ",
                "Votre préavis de " + jours + " jours est enregistré. Fin du bail le " + Utils.date(b.getDateFin()) + ".");
        return true;
    }

    public boolean sortir(String idBail, LocalDate date, double retenuesDegats) {
        Bail b = db.trouverBail(idBail);
        if (b == null || b.getStatut() != StatutBail.PREAVIS) {
            System.out.println("Sortie impossible pour " + idBail);
            return false;
        }
        if (date.isBefore(b.getDateFin())) {
            System.out.println("Sortie impossible avant la fin du préavis (" + Utils.date(b.getDateFin()) + ")");
            return false;
        }
        Logement l = b.getLogement();
        if (l instanceof Colocation) {
            l.retirerColocataire(b.getEtudiant());
            l.nettoyerPartiesCommunes();
        } else {
            l.liberer(b.getEtudiant());
        }

        double impayes = b.soldeDu > 0 ? b.soldeDu : 0;
        double restitution = b.getCaution() - retenuesDegats - impayes;
        double resteAPayer = 0;
        if (restitution < 0) {
            resteAPayer = -restitution;
            restitution = 0;
        }
        b.retenues = Utils.arrondir(retenuesDegats);
        b.cautionRestituee = Utils.arrondir(restitution);
        b.soldeDu = Utils.arrondir(resteAPayer);
        b.setStatut(StatutBail.TERMINE);
        db.mettreAJourBail(b);

        System.out.println("Sortie " + idBail + " le " + Utils.date(date) + " : caution " + Utils.euros(b.getCaution())
                + ", retenues " + Utils.euros(b.retenues) + ", impayés " + Utils.euros(impayes)
                + " -> restitué " + Utils.euros(b.cautionRestituee));
        String message = "Votre dépôt de garantie : " + Utils.euros(b.cautionRestituee) + " vous sera restitué sous 30 jours.";
        if (resteAPayer > 0) {
            message = message + " Il reste " + Utils.euros(b.soldeDu) + " à régler.";
        }
        notificationService.notifier(b.getEtudiant(), "Sortie du logement " + l.getNumero(), message);
        return true;
    }

    public boolean annulerBail(String idBail) {
        Bail b = db.trouverBail(idBail);
        if (b == null || b.getStatut() != StatutBail.SIGNE) {
            System.out.println("Annulation impossible pour " + idBail);
            return false;
        }
        Logement l = b.getLogement();
        if (l instanceof Colocation) {
            l.retirerColocataire(b.getEtudiant());
        } else {
            l.liberer(b.getEtudiant());
        }
        b.setStatut(StatutBail.ANNULE);
        db.mettreAJourBail(b);
        System.out.println("Bail " + idBail + " annulé, frais de dossier conservés : " + Utils.euros(b.getFraisDossier()));
        notificationService.notifier(b.getEtudiant(), "Annulation du bail " + idBail,
                "Votre bail est annulé. Les frais de dossier (" + Utils.euros(b.getFraisDossier()) + ") restent acquis.");
        return true;
    }

    // ------------------------------------------------------------------ relances et rapport

    public void relancerImpayes() {
        System.out.println("--- Relance des impayés");
        for (Bail b : db.tousLesBaux()) {
            if (b.soldeDu <= 0) continue;
            if (b.getStatut() == StatutBail.ANNULE) continue;
            notificationService.relancerImpayes(b);
        }
    }
}
