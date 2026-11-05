package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServicesNotificationTest {

    @Test
    void journalsEtCompteursSontPropresAChaqueGrapheDeServices() {
        EmailService premiersEmails = new EmailService();
        SmsService premiersSms = new SmsService();
        CourrierService premiersCourriers = new CourrierService();
        ServiceNotification premierGraphe = new ServiceNotification(premiersEmails, premiersSms, premiersCourriers);

        Etudiant etudiant = new Etudiant("ETU-001", "Martin", "Alice", "alice@example.fr", "0600000000", null,
                ProfilEtudiant.STANDARD, 0, 1500, false, CanalNotification.SMS);
        premierGraphe.notifier(etudiant, "Sujet", "Message");

        ServiceNotification secondGraphe = new ServiceNotification(new EmailService(), new SmsService(),
                new CourrierService());

        assertEquals(1, premierGraphe.getNombreSmsEnvoyes());
        assertEquals(1, premierGraphe.getSmsJournal().size());
        assertEquals(0, secondGraphe.getNombreSmsEnvoyes());
        assertEquals(0, secondGraphe.getSmsJournal().size());
        assertEquals(0, premierGraphe.getEmailJournal().size());
        assertEquals(0, premierGraphe.getCourrierJournal().size());
    }
}
