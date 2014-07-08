package pl.matsuo.core.model.kv;

import pl.matsuo.core.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderColumn;
import java.util.HashMap;
import java.util.Map;


@MappedSuperclass
public class KeyValueEntity extends AbstractEntity {


  @ElementCollection
  @OrderColumn
  @Lob
  @Column(length = 32768)
  private final Map<String, String> fields = new HashMap<String, String>();


  public Map<String, String> getFields() {
    return fields;
  }
}

