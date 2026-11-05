package fr.efrei.residence;

public interface NotificationService {

    void notifier(Etudiant etudiant, String sujet, String message);

    void relancerImpayes(Bail bail);
}
