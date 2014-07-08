package pl.matsuo.core.model.message;

import javax.persistence.Entity;

/**
 * Created by marek on 07.07.14.
 */
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
