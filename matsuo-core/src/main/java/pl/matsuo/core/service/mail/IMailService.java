package pl.matsuo.core.service.mail;

/**
 * Created by marek on 24.01.15.
 */
public interface IMailService {


  Integer sendMail(String from, String to, String subject, String bodyTemplate, Object model);
}
