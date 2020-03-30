package pl.matsuo.core.model.message;

import javax.persistence.Entity;

@Entity
public class MailMessage extends AbstractMessage {

  private String address;

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
