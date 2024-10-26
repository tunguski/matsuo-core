package pl.matsuo.core.web.view;

import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;
import java.sql.Time;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.validation.EntityReference;
import pl.matsuo.core.model.validation.PasswordField;

@Getter
@Setter
public class TestModel {

  @EntityReference(value = TestReferenceModel.class)
  public Integer reference;

  @ManyToOne public TestReferenceModel subModel;
  public Integer duration;
  public String text;
  @PasswordField public String password;
  public Date date;
  public Time time;
  public EnumType enumValue;
  public boolean bool;
}
