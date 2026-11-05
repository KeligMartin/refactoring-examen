package fr.efrei.residence;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/**
 * Service principal de la résidence : inscriptions, attributions, baux, loyers, préavis, sorties.
 */
public class GestionResidence {

    public static int compteurBail = 0;

    private final EtudiantRepository etudiants;
    private final BailRepository baux;
    private final LogementRepository logements;
    private CalculateurLoyer calculateur = new CalculateurLoyer();
    private final NotificationService notificationService;
    private final ValidateurInscription validateurInscription;
    private final ServiceAttribution serviceAttribution;
    private final CycleVieBailService cycleVieBailService;

    public GestionResidence(EtudiantRepository etudiants, BailRepository baux, LogementRepository logements,
                            NotificationService notificationService, ValidateurInscription validateurInscription,
                            ServiceAttribution serviceAttribution, CycleVieBailService cycleVieBailService) {
        this.etudiants = Objects.requireNonNull(etudiants, "etudiants");
        this.baux = Objects.requireNonNull(baux, "baux");
        this.logements = Objects.requireNonNull(logements, "logements");
        this.notificationService = Objects.requireNonNull(notificationService, "notificationService");
        this.validateurInscription = Objects.requireNonNull(validateurInscription, "validateurInscription");
        this.serviceAttribution = Objects.requireNonNull(serviceAttribution, "serviceAttribution");
        this.cycleVieBailService = Objects.requireNonNull(cycleVieBailService, "cycleVieBailService");
    }

    // ------------------------------------------------------------------ étudiants

    public Etudiant inscrireEtudiant(String nom, String prenom, String email, String telephone, String adresse,
                                     ProfilEtudiant profil, int echelonBourse, double ressources, boolean pmr,
                                     CanalNotification canalPrefere) {
        String motif = validateurInscription.motifRefus(nom, email, telephone, adresse, profil, echelonBourse,
                canalPrefere, adresseEmail -> etudiants.trouverParEmail(adresseEmail) != null);
        if (motif != null) {
            System.out.println(motif);
            return null;
        }
        Etudiant e = new Etudiant(nom, prenom, email, telephone, adresse, profil, echelonBourse, ressources, pmr,
                canalPrefere);
        etudiants.sauvegarder(e);
        System.out.println("Étudiant inscrit : " + e.getId() + " " + e.getNomComplet() + " (" + profil + ")");
        return e;
    }

    // ------------------------------------------------------------------ logements

    public Logement ajouterLogement(TypeLogement type, String numero, double surface, int etage, boolean meuble, int param) {
        if (logements.trouverParNumero(numero) != null) {
            System.out.println("Logement " + numero + " existe déjà");
            return null;
        }
        Logement l = LogementFactory.creer(type, numero, surface, etage, meuble, param);
        if (l == null) {
            System.out.println("Type de logement inconnu : " + type);
            return null;
        }
        logements.sauvegarder(l);
        System.out.println("Logement ajouté : " + numero + " (" + type + ")");
        return l;
    }

    // ------------------------------------------------------------------ attribution

    public Bail attribuerLogement(Etudiant e, TypeLogement typeSouhaite, Set<OptionLoyer> options, LocalDate dateSignature) {
        ResultatAttribution resultat = serviceAttribution.attribuer(e, typeSouhaite, options, logements.tous(),
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

        baux.sauvegarder(b);
        System.out.println("  Bail " + id + " signé : " + choisi.getNumero() + ", " + Utils.euros(b.getLoyerChargesComprises())
                + " CC/mois, caution " + Utils.euros(b.getCaution()) + ", frais de dossier " + Utils.euros(b.getFraisDossier()));
        notificationService.notifier(e, "Votre bail " + id,
                "Bienvenue à la " + Residence.NOM + " ! Logement " + choisi.getNumero() + ", loyer "
                        + Utils.euros(b.getLoyerChargesComprises()) + " charges comprises.");
        return b;
    }

    // ------------------------------------------------------------------ cycle de vie du bail

    public boolean entrerDansLesLieux(String idBail, LocalDate date) {
        return cycleVieBailService.entrerDansLesLieux(idBail, date);
    }

    public boolean payerLoyer(String idBail, String mois, LocalDate datePaiement, double montant) {
        return cycleVieBailService.payerLoyer(idBail, mois, datePaiement, montant);
    }

    public boolean donnerPreavis(String idBail, LocalDate date) {
        return cycleVieBailService.donnerPreavis(idBail, date);
    }

    public boolean sortir(String idBail, LocalDate date, double retenuesDegats) {
        return cycleVieBailService.sortir(idBail, date, retenuesDegats);
    }

    public boolean annulerBail(String idBail) {
        return cycleVieBailService.annulerBail(idBail);
    }

    // ------------------------------------------------------------------ relances et rapport

    public void relancerImpayes() {
        cycleVieBailService.relancerImpayes();
    }
}
