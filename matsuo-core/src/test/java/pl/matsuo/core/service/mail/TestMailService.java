package pl.matsuo.core.service.mail;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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

@RunWith(MockitoJUnitRunner.class)
public class TestMailService {

  @InjectMocks MailService mailService = new MailService();
  @Spy JavaMailSender mailSender = new JavaMailSenderImpl();
  @Mock Database database;
  @Mock IPrintsRendererService printsRendererService;

  public TestMailService() {
    mailService.database = database;
    mailService.mailSender = mailSender;
  }

  @Test
  public void testSendMail() throws AddressException {
    MailMessage mailMessage = new MailMessage();
    mailMessage.setId(55L);

    doAnswer(invocation -> null).when(mailSender).send(any(MimeMessage.class));
    when(database.create(any(MailMessage.class))).thenReturn(mailMessage);

    when(printsRendererService.renderHtml("bodyTemplate.ftl", null)).thenReturn("OK".getBytes());

    assertEquals(
        (Long) 55L,
        mailService.sendMail(
            new InternetAddress("from@example.com"),
            new InternetAddress("to@example.com"),
            "subject",
            "bodyTemplate.ftl",
            null));

    verify(mailSender).send(any(MimeMessage.class));
  }
}
