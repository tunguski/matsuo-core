package pl.matsuo.core.model.message;

import javax.persistence.Entity;

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
