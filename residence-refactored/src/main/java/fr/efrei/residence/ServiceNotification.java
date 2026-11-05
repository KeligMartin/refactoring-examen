package fr.efrei.residence;

public class ServiceNotification implements NotificationService {

    @Override
    public void notifier(Etudiant etudiant, String sujet, String message) {
        CanalNotification canal = etudiant.getCanalPrefere();
        if (canal == CanalNotification.EMAIL) {
            EmailService.envoyer(etudiant.getEmail(), sujet, message);
        } else if (canal == CanalNotification.SMS) {
            ServiceNotificationFactory.creerSms().envoyerSms(etudiant.getTelephone(), sujet + " - " + message);
        } else if (canal == CanalNotification.COURRIER) {
            ServiceNotificationFactory.creerCourrier().imprimerEtPoster(
                    etudiant.getNomComplet(), etudiant.getAdresse(), sujet, message);
        }
        if (PolitiqueProfil.pour(etudiant.getProfil()).recoitEmailComplementaire(canal)) {
            EmailService.envoyer(etudiant.getEmail(), "[EN] " + sujet, message);
        }
    }

    @Override
    public void relancerImpayes(Bail bail) {
        Etudiant etudiant = bail.getEtudiant();
        CanalNotification canal = etudiant.getCanalPrefere();
        switch (canal) {
            case EMAIL:
                EmailService.envoyer(etudiant.getEmail(), "RELANCE - loyer impayé",
                        "Bonjour " + etudiant.getPrenom() + ", il reste " + Utils.euros(bail.soldeDu)
                                + " à régler sur le bail " + bail.getId() + ".");
                break;
            case SMS:
                ServiceNotificationFactory.creerSms().envoyerSms(etudiant.getTelephone(),
                        "Campus Vert : " + Utils.euros(bail.soldeDu) + " impayés sur " + bail.getId());
                break;
            case COURRIER:
                ServiceNotificationFactory.creerCourrier().imprimerEtPoster(etudiant.getNomComplet(), etudiant.getAdresse(),
                        "Relance loyer impayé",
                        "Il reste " + Utils.euros(bail.soldeDu) + " à régler sur le bail " + bail.getId() + ".");
                break;
            default:
                System.out.println("Canal inconnu " + canal);
        }
    }
}
