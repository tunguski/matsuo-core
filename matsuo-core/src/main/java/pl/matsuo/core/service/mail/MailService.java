package pl.matsuo.core.service.mail;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.matsuo.core.model.message.MailMessage;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.print.IPrintsRendererService;

import javax.mail.internet.MimeMessage;

/**
 * Created by marek on 24.01.15.
 */
@Service
public class MailService implements IMailService {


  @Autowired
  IPrintsRendererService printsRendererService;
  @Autowired
  JavaMailSender mailSender;
  @Autowired
  Database database;


  public Integer sendMail(String from, String to, String subject, String bodyTemplate, Object model) {
    try{
      MimeMessage message = mailSender.createMimeMessage();

      // use the true flag to indicate you need a multipart message
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setTo(to);
      helper.setFrom(from);
      helper.setSubject(subject);

      String body = new String(printsRendererService.renderHtml(bodyTemplate, model));

      // use the true flag to indicate the text included is HTML
      helper.setText(body, true);
      mailSender.send(message);

      // after success create database entity
      MailMessage mail = new MailMessage();
      mail.setAddress(to);
      mail.setText(body);

      return database.create(mail).getId();

    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}

