package pl.matsuo.core.service.mail;

import jakarta.mail.internet.InternetAddress;

public interface IMailService {

  Long sendMail(
      InternetAddress from, InternetAddress to, String subject, String bodyTemplate, Object model);
}
