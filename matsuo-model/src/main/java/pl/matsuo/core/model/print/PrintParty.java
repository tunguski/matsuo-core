package pl.matsuo.core.model.print;

import pl.matsuo.core.model.kv.IKeyValueFacade;
import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.model.validation.EntityReference;

public interface PrintParty extends IKeyValueFacade {

  @EntityReference(value = AbstractParty.class)
  Integer getId();

  void setId(Integer id);

  String getName();

  void setName(String name);

  String getAddress();

  void setAddress(String address);

  String getNip();

  void setNip(String nip);

  String getPesel();

  void setPesel(String pesel);
}
