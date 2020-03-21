package pl.matsuo.core.model.organization.address;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestCountryType {

  @Test
  public void testNotNull() {
    for (CountryType countryType : CountryType.values()) {
      assertNotNull(countryType.name);
      assertNotNull(countryType.longCode);
      assertNotNull(countryType.numberCode);
    }
  }
}
