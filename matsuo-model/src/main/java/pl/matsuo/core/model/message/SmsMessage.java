package pl.matsuo.core.model.message;

import javax.persistence.Entity;

/** Created by marek on 07.07.14. */
@Entity
public class SmsMessage extends AbstractMessage {

  private String phoneNumber;

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}
