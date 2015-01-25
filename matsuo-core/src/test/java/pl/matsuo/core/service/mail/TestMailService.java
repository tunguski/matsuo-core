package pl.matsuo.core.service.mail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import pl.matsuo.core.model.message.MailMessage;
import pl.matsuo.core.service.db.Database;

import javax.mail.internet.MimeMessage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class TestMailService {


  @InjectMocks
  MailService mailService = new MailService();
  @Mock
  JavaMailSender mailSender;
  @Mock
  Database database;


  public TestMailService() {
    mailService.database = database;
    mailService.mailSender = mailSender;
  }


  @Test
  public void testSendMail() throws Exception {
    MailMessage mailMessage = new MailMessage();
    mailMessage.setId(55);

    MimeMessage mimeMessage = mock(MimeMessage.class);
    when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    when(database.create(any(MailMessage.class))).thenReturn(mailMessage);

    assertEquals((Integer) 55, mailService.sendMail("from", "to", "subject", "body"));

    verify(mailSender).send(any(MimeMessage.class));
  }
}

