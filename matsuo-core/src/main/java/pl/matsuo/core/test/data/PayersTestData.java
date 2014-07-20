package pl.matsuo.core.test.data;


import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.matsuo.core.conf.DiscoverTypes;
import pl.matsuo.core.model.organization.OrganizationUnit;
import pl.matsuo.core.model.organization.Person;

import java.util.Collections;
import java.util.List;

import static pl.matsuo.core.model.query.QueryBuilder.*;
import static pl.matsuo.core.test.data.MediqTestData.*;


@Component
@Order(10)
@DiscoverTypes({ MediqTestData.class })
public class PayersTestData extends AbstractMediqTestData {


  public static final String ONET = "ONET";
  public static final String GETIN = "Getin";
  public static final String DELL = "DELL";
  public static final String ABC = "ABC";
  public static final String IT = "IT";
  public static final String MPWIK_LEG = "MPWIK LEG";
  public static final String ITAKA = "ITAKA";
  public static final String PZU_LEG = "PZU LEG";
  public static final String ZDZIWKO = "ZDZIWKO";
  public static final String AWIOTEX = "Awiotex";
  public static final String BANASZEK = "Banaszek";
  public static final String REMONT = "REMONT";
  public static final String KOMBATANCI = "Kombatanci";


  @Override
  public void internalExecute() {
    createCompany("Mediq - Sejf", MEDIQ, "Legionowo", "Piłsudskiego 20", "5361188849", "010313501");

    createCompany(KOMBATANCI, KOMBATANCI, "Legionowo", "Piłsudskiego 20", "5361188849",
        "010313501", "53041429507", "73020514008", "58021940250");

    createCompany("Onet sp. z o.o.", ONET, "Warszawa", "Sobieskiego", "7340009469", "001337730",
        "53041429507", "58021940250", "40112171441");

    createCompany("Getin Bank", GETIN, "Legionowo", "Piłsudskiego", "6340194590", "003538527",
        "66090199084", "29032882677", "43093090369", "90083139483");

    createCompany("Dell Company sp. z o.o.", DELL, "Warszawa", "Aleje Jerozolimskie 147",
        "6340194590", "003538527", "50100698500", "73020514008", "42020446855");

    createCompany("ABC DATA", ABC, "Warszawa", "Solec 12", "5242617178", "141054682",
        "53041429507", "58021940250", "40112171441");

    createCompany("IT Develop", IT, "Warszawa", "Modlińska 256", "6282159179", "120461700",
        "53041429507", "58021940250", "40112171441", "56062294228", "54072634553", "39021867026",
        "67020209961", "32010601753", "88122464018", "33041203367");

    createCompany("MPWiK Legionowo", MPWIK_LEG, "Legionowo", "Chopina 12", "5250005662",
        "015314758", "53041429507", "58021940250", "40112171441");

    createCompany("Itaka", ITAKA, "Legionowo", "Sobieskiego 23", "7542686316", "532179139",
        "63082812545", "33112736370", "39102319493", "31122128435", "54011351550", "86082439303",
        "64120333581");

    createCompany("PZU oddział terenowy Legionowo", PZU_LEG, "Legionowo", "Mrugacza",
        "5262222336", "013104910", "53041429507", "58021940250", "40112171441");

    createCompany("PHU Zdziwko Andrzej", ZDZIWKO, "Warszawa", "Polna 56", "6570231024",
        "003670267", "83111127198", "56091969821", "67051034420", "90012302690");

    createCompany("Awiotex Legionowo", AWIOTEX, "Legionowo", "Warszawska 124", "5360007159",
        "012592304", "83061114361", "58021940250", "40112171441");

    createCompany("Banaszek rowery", BANASZEK, "Legionowo", "Zegrzyńska 13", "5361869555",
        "142000040", "53041429507", "58021940250", "40112171441");

    createCompany("PHU REMONT", REMONT, "Legionowo", "Sielankowa", "1230826891", "013281894",
        "53041429507", "71082934187", "40112171441");
  }


  private OrganizationUnit createCompany(String fullName, String code, String town, String street, String nip,
      String regon, String ... patientPesels) {
    OrganizationUnit organizationUnit = new OrganizationUnit();

    organizationUnit.setFullName(fullName);
    organizationUnit.setCode(code);
    organizationUnit.getAddress().setTown(town);
    organizationUnit.getAddress().setStreet(street);
    organizationUnit.setNip(nip);
    organizationUnit.setRegon(regon);

    List<Person> employees =
      patientPesels.length > 0 ? database.find(query(Person.class, in("pesel", patientPesels)))
        : Collections.<Person>emptyList();

    organizationUnit.getEmployees().addAll(employees);
    return database.create(organizationUnit);
  }


  @Override
  public String getExecuteServiceName() {
    return getClass().getName();
  }
}

