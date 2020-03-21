package pl.matsuo.core.kv;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.MappedSuperclass;
import pl.matsuo.core.model.kv.KeyValueEntity;

@MappedSuperclass
public class TestKeyValueSubEntity extends KeyValueEntity {

  protected List<TestKeyValueSubEntity> internalElements = new ArrayList<>();

  List<TestKeyValueSubEntity> getInternalElements() {
    return internalElements;
  }
}
