package pl.matsuo.core.model.organization.address;

import org.junit.Test;

import static org.junit.Assert.*;


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

