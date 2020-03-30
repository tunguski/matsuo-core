package pl.matsuo.core.service.mail;

import javax.mail.internet.InternetAddress;

public interface IMailService {

  Integer sendMail(
      InternetAddress from, InternetAddress to, String subject, String bodyTemplate, Object model);
}
