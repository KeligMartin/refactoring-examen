package fr.efrei.residence;

import java.time.LocalDate;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        EtudiantRepository etudiants = new DepotEtudiantMemoire();
        BailRepository baux = new DepotBailMemoire();
        LogementRepository logements = new DepotLogementMemoire();
        Residence.creerLogementsInitiaux().forEach(logements::sauvegarder);
        EmailService emailService = new EmailService();
        SmsService smsService = new SmsService();
        CourrierService courrierService = new CourrierService();
        ServiceNotification serviceNotification = new ServiceNotification(emailService, smsService, courrierService);
        CalculateurLoyer calculateur = new CalculateurLoyer(new AssembleurLoyer());
        GestionResidence gestion = new GestionResidence(etudiants, baux, logements, serviceNotification,
                new ValidateurInscription(), new ServiceAttribution(),
                new CycleVieBailService(baux, serviceNotification), new IdentifiantSequence(), calculateur);

        gestion.ajouterLogement(TypeLogement.STUDIO, "C002", 16, 0, false, 0);

        System.out.println();
        Etudiant alice = gestion.inscrireEtudiant("Martin", "Alice", "alice.martin@mail.fr", "0601020304", null,
                ProfilEtudiant.STANDARD, 0, 1500, false, CanalNotification.EMAIL);
        Etudiant bilal = gestion.inscrireEtudiant("Haddad", "Bilal", "bilal.haddad@mail.fr", "0611223344", null,
                ProfilEtudiant.BOURSIER, 5, 450, false, CanalNotification.SMS);
        Etudiant chloe = gestion.inscrireEtudiant("Durand", "Chloé", "chloe.durand@mail.fr", "0622334455", null,
                ProfilEtudiant.ALTERNANT, 0, 1100, true, CanalNotification.EMAIL);
        Etudiant diego = gestion.inscrireEtudiant("Alvarez", "Diego", "diego.alvarez@mail.es", "+34612345678", null,
                ProfilEtudiant.INTERNATIONAL, 0, 3000, false, CanalNotification.SMS);
        Etudiant emma = gestion.inscrireEtudiant("Petit", "Emma", "emma.petit@mail.fr", null,
                "12 rue des Lilas, 35000 Rennes", ProfilEtudiant.STANDARD, 0, 1300, false, CanalNotification.COURRIER);
        Etudiant gaelle = gestion.inscrireEtudiant("Morel", "Gaëlle", "gaelle.morel@mail.fr", "0633445566", null,
                ProfilEtudiant.STANDARD, 0, 1000, false, CanalNotification.EMAIL);
        Etudiant hugo = gestion.inscrireEtudiant("Leroy", "Hugo", "hugo.leroy@mail.fr", "0644556677", null,
                ProfilEtudiant.BOURSIER, 7, 300, true, CanalNotification.EMAIL);
        gestion.inscrireEtudiant("Faure", "Farid", "farid.faure-at-mail.fr", "0655667788", null,
                ProfilEtudiant.STANDARD, 0, 1200, false, CanalNotification.EMAIL);
        gestion.inscrireEtudiant("Wang", "Li", "li.wang@mail.cn", "0612121212", null,
                ProfilEtudiant.INTERNATIONAL, 0, 2000, false, CanalNotification.EMAIL);
        gestion.inscrireEtudiant("Doublon", "Alice", "alice.martin@mail.fr", "0601020304", null,
                ProfilEtudiant.STANDARD, 0, 1500, false, CanalNotification.EMAIL);

        System.out.println();
        LocalDate signature = LocalDate.of(2026, 8, 25);
        Bail bA = gestion.attribuerLogement(alice, TypeLogement.STUDIO, Set.of(OptionLoyer.PARKING, OptionLoyer.ASSURANCE), signature);
        Bail bB = gestion.attribuerLogement(bilal, TypeLogement.COLOCATION, Set.of(OptionLoyer.MENAGE, OptionLoyer.LAVERIE,
                OptionLoyer.FIBRE), signature);
        Bail bC = gestion.attribuerLogement(chloe, TypeLogement.T1, Set.of(OptionLoyer.LAVERIE, OptionLoyer.FIBRE,
                OptionLoyer.ASSURANCE), signature);
        Bail bD = gestion.attribuerLogement(diego, TypeLogement.T2, Set.of(OptionLoyer.PARKING, OptionLoyer.MENAGE,
                OptionLoyer.FIBRE, OptionLoyer.ASSURANCE), signature);
        Bail bE = gestion.attribuerLogement(emma, TypeLogement.COLOCATION, Set.of(OptionLoyer.FIBRE), signature);
        Bail bG = gestion.attribuerLogement(gaelle, TypeLogement.STUDIO, Set.of(OptionLoyer.MENAGE, OptionLoyer.FIBRE), signature);
        Bail bH = gestion.attribuerLogement(hugo, TypeLogement.COLOCATION, Set.of(OptionLoyer.MENAGE, OptionLoyer.ASSURANCE),
                signature);

        System.out.println();
        LocalDate rentree = LocalDate.of(2026, 9, 1);
        gestion.entrerDansLesLieux(bA.getId(), rentree);
        gestion.entrerDansLesLieux(bB.getId(), rentree);
        gestion.entrerDansLesLieux(bC.getId(), rentree);
        gestion.entrerDansLesLieux(bD.getId(), rentree);
        gestion.entrerDansLesLieux(bE.getId(), LocalDate.of(2026, 8, 20));
        gestion.entrerDansLesLieux(bE.getId(), rentree);
        if (bH != null) gestion.annulerBail(bH.getId());
        if (bG != null) gestion.annulerBail(bG.getId());

        System.out.println();
        gestion.payerLoyer(bA.getId(), "2026-09", LocalDate.of(2026, 9, 3), bA.getLoyerChargesComprises());
        gestion.payerLoyer(bB.getId(), "2026-09", LocalDate.of(2026, 9, 12), bB.getLoyerChargesComprises());
        gestion.payerLoyer(bC.getId(), "2026-09", LocalDate.of(2026, 9, 5), bC.getLoyerChargesComprises());
        gestion.payerLoyer(bD.getId(), "2026-09", LocalDate.of(2026, 9, 9), bD.getLoyerChargesComprises());
        gestion.payerLoyer(bE.getId(), "2026-09", LocalDate.of(2026, 9, 2), 200);
        gestion.payerLoyer(bA.getId(), "2026-10", LocalDate.of(2026, 10, 15), 300);
        gestion.payerLoyer(bC.getId(), "2026-10", LocalDate.of(2026, 10, 1), 1000);

        System.out.println();
        QuittanceGenerateur quittanceGenerateur = new QuittanceGenerateur();
        System.out.println(quittanceGenerateur.generer(bD, "2026-09"));
        System.out.println(quittanceGenerateur.generer(bA, "2026-10"));

        System.out.println();
        gestion.relancerImpayes();

        System.out.println();
        gestion.donnerPreavis(bB.getId(), LocalDate.of(2026, 10, 20));
        gestion.donnerPreavis(bD.getId(), LocalDate.of(2026, 10, 20));
        gestion.donnerPreavis(bE.getId(), LocalDate.of(2026, 10, 20));
        gestion.sortir(bD.getId(), LocalDate.of(2026, 11, 5), 0);
        gestion.sortir(bB.getId(), LocalDate.of(2026, 11, 5), 40);
        gestion.sortir(bD.getId(), LocalDate.of(2026, 11, 20), 120);
        gestion.sortir(bE.getId(), LocalDate.of(2026, 11, 20), 30);

        new RapportGenerateur().afficher(logements.tous(), baux.tous(),
                serviceNotification.getNombreSmsEnvoyes(), serviceNotification.getAffranchissement());
    }
}
