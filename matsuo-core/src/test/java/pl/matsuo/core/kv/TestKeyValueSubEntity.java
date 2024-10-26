package pl.matsuo.core.kv;

import jakarta.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import pl.matsuo.core.model.kv.KeyValueEntity;

@MappedSuperclass
@Getter
public class TestKeyValueSubEntity extends KeyValueEntity {

  protected List<TestKeyValueSubEntity> internalElements = new ArrayList<>();
}
