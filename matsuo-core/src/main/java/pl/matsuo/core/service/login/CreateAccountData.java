package pl.matsuo.core.service.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountData extends LoginData {

  private String companyName;
  private String companyShortName;
  private String companyNip;
}
