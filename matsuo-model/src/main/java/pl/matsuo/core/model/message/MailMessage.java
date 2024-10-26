package pl.matsuo.core.model.message;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MailMessage extends AbstractMessage {

  private String address;
}
