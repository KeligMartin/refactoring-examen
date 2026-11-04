package fr.efrei.residence;

public class ServiceNotificationFactory {

    public static SmsService creerSms() {
        return new SmsService();
    }

    public static CourrierService creerCourrier() {
        return new CourrierService();
    }
}
