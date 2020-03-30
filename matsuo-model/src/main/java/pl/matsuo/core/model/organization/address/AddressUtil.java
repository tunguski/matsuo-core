package pl.matsuo.core.model.organization.address;

public class AddressUtil {

  /** Zamienia <i>null</i> na pusty string */
  public static String noNull(String input) {
    return (input == null) ? "" : input;
  }

  public static String htmlAddress(Address address) {
    return noNull(address.getStreet())
        + "<br/>"
        + noNull(address.getZipCode())
        + " "
        + noNull(address.getTown());
  }
}
