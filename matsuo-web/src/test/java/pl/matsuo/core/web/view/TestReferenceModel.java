package pl.matsuo.core.web.view;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.validation.EntityReference;

@Getter
@Setter
public class TestReferenceModel extends AbstractEntity {

  Date date;

  @EntityReference(value = TestReferenceModel.class)
  @Override
  public Long getId() {
    return super.getId();
  }
}
