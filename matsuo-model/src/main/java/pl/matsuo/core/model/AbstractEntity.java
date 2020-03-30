package pl.matsuo.core.model;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.api.HasId;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity implements HasId, Comparable<AbstractEntity> {

  @Id
  @GeneratedValue(strategy = SEQUENCE, generator = "AbstractEntitySequence")
  @SequenceGenerator(name = "AbstractEntitySequence", sequenceName = "entity_seq")
  protected Integer id;

  protected Date createdTime;
  /**
   * This is id defining most general data association to privileges. In most common situation this
   * id is user id or company id (data aggregated for many users in organization). This id must
   * always match user's idBucket.
   *
   * <p>All client data must have idBucket assigned. Only it administrative data (like logs from
   * logged-off users) may be disconnected.
   */
  protected Integer idBucket;

  @Override
  public int compareTo(AbstractEntity entity) {
    return id.compareTo(entity.getId());
  }
}
