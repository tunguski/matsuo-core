package pl.matsuo.core.model.message;

import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SmsMessage extends AbstractMessage {

  private String phoneNumber;
}
