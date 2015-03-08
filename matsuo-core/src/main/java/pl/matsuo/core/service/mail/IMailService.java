package pl.matsuo.core.service.mail;

import javax.mail.internet.InternetAddress;

/**
 * Created by marek on 24.01.15.
 */
public interface IMailService {


  Integer sendMail(InternetAddress from, InternetAddress to, String subject, String bodyTemplate, Object model);
}
