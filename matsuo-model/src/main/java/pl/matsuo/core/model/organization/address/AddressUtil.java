package pl.matsuo.core.model.organization.address;


/**
 * Created by marek on 30.03.14.
 */
public class AddressUtil {


  /**
   * Zamienia <i>null</i> na pusty string
   */
  public static String noNull(String input) {
    return (input == null) ? "" : input;
  }


  public static String htmlAddress(Address address) {
    return noNull(address.getStreet())
        + "<br/>" + noNull(address.getZipCode()) + " " + noNull(address.getTown());
  }
}

