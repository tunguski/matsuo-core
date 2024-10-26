package pl.matsuo.core.model.kv;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Lob;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OrderColumn;
import java.util.HashMap;
import java.util.Map;
import pl.matsuo.core.model.AbstractEntity;

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
