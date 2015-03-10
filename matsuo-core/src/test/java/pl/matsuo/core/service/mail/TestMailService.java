package pl.matsuo.core.service.mail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import pl.matsuo.core.model.message.MailMessage;
import pl.matsuo.core.service.db.Database;
import pl.matsuo.core.service.print.IPrintsRendererService;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class TestMailService {


  @InjectMocks
  MailService mailService = new MailService();
  @Spy
  JavaMailSender mailSender = new JavaMailSenderImpl();
  @Mock
  Database database;
  @Mock
  IPrintsRendererService printsRendererService;


  public TestMailService() {
    mailService.database = database;
    mailService.mailSender = mailSender;
  }


  @Test
  public void testSendMail() throws Exception {
    MailMessage mailMessage = new MailMessage();
    mailMessage.setId(55);

    doAnswer(invocation -> null).when(mailSender).send(any(MimeMessage.class));
    when(database.create(any(MailMessage.class))).thenReturn(mailMessage);

    when(printsRendererService.renderHtml("bodyTemplate.ftl", null)).thenReturn("OK".getBytes());

    assertEquals((Integer) 55, mailService.sendMail(new InternetAddress("from@example.com"),
        new InternetAddress("to@example.com"), "subject", "bodyTemplate.ftl", null));


    verify(mailSender).send(any(MimeMessage.class));
  }
}

