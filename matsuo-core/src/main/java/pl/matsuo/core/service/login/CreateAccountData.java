package pl.matsuo.core.service.login;

public class CreateAccountData extends LoginData {

  private String companyName;
  private String companyShortName;
  private String companyNip;

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getCompanyShortName() {
    return companyShortName;
  }

  public void setCompanyShortName(String companyShortName) {
    this.companyShortName = companyShortName;
  }

  public String getCompanyNip() {
    return companyNip;
  }

  public void setCompanyNip(String companyNip) {
    this.companyNip = companyNip;
  }
}
