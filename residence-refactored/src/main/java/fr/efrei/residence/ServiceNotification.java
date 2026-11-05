package fr.efrei.residence;

public class ServiceNotification implements NotificationService {

    private final EmailService emailService;
    private final SmsService smsService;
    private final CourrierService courrierService;

    public ServiceNotification(EmailService emailService, SmsService smsService, CourrierService courrierService) {
        this.emailService = java.util.Objects.requireNonNull(emailService, "emailService");
        this.smsService = java.util.Objects.requireNonNull(smsService, "smsService");
        this.courrierService = java.util.Objects.requireNonNull(courrierService, "courrierService");
    }

    @Override
    public void notifier(Etudiant etudiant, String sujet, String message) {
        CanalNotification canal = etudiant.getCanalPrefere();
        if (canal == CanalNotification.EMAIL) {
            emailService.envoyer(etudiant.getEmail(), sujet, message);
        } else if (canal == CanalNotification.SMS) {
            smsService.envoyerSms(etudiant.getTelephone(), sujet + " - " + message);
        } else if (canal == CanalNotification.COURRIER) {
            courrierService.imprimerEtPoster(
                    etudiant.getNomComplet(), etudiant.getAdresse(), sujet, message);
        }
        if (PolitiqueProfil.pour(etudiant.getProfil()).recoitEmailComplementaire(canal)) {
            emailService.envoyer(etudiant.getEmail(), "[EN] " + sujet, message);
        }
    }

    @Override
    public void relancerImpayes(Bail bail) {
        Etudiant etudiant = bail.getEtudiant();
        CanalNotification canal = etudiant.getCanalPrefere();
        switch (canal) {
            case EMAIL:
                emailService.envoyer(etudiant.getEmail(), "RELANCE - loyer impayé",
                        "Bonjour " + etudiant.getPrenom() + ", il reste " + Utils.euros(bail.soldeDu)
                                + " à régler sur le bail " + bail.getId() + ".");
                break;
            case SMS:
                smsService.envoyerSms(etudiant.getTelephone(),
                        "Campus Vert : " + Utils.euros(bail.soldeDu) + " impayés sur " + bail.getId());
                break;
            case COURRIER:
                courrierService.imprimerEtPoster(etudiant.getNomComplet(), etudiant.getAdresse(),
                        "Relance loyer impayé",
                        "Il reste " + Utils.euros(bail.soldeDu) + " à régler sur le bail " + bail.getId() + ".");
                break;
            default:
                System.out.println("Canal inconnu " + canal);
        }
    }

    public java.util.List<String[]> getEmailJournal() {
        return emailService.getJournal();
    }

    public java.util.List<String[]> getSmsJournal() {
        return smsService.getJournal();
    }

    public java.util.List<String[]> getCourrierJournal() {
        return courrierService.getJournal();
    }

    public int getNombreSmsEnvoyes() {
        return smsService.getCompteurSms();
    }

    public double getAffranchissement() {
        return courrierService.getTotalAffranchissement();
    }
}
