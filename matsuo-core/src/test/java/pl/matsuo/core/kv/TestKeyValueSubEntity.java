package pl.matsuo.core.kv;

import pl.matsuo.core.model.kv.KeyValueEntity;

import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.List;


@MappedSuperclass
public class TestKeyValueSubEntity extends KeyValueEntity {

  protected List<TestKeyValueSubEntity> internalElements = new ArrayList<>();
  List<TestKeyValueSubEntity> getInternalElements() {
    return internalElements;
  }
}

