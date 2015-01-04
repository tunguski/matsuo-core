package pl.matsuo.core.model;

import pl.matsuo.core.model.api.HasId;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import java.util.Date;

import static javax.persistence.GenerationType.*;


@MappedSuperclass
public abstract class AbstractEntity implements HasId, Comparable<AbstractEntity> {


  @Id
  @GeneratedValue(strategy = SEQUENCE, generator = "AbstractEntitySequence")
  @SequenceGenerator(name = "AbstractEntitySequence", sequenceName = "entity_seq")
  protected Integer id;
  protected Date createdTime;
  /**
   * <p>
   * This is id defining most general data association to privileges. In most common situation this id is
   * user id or company id (data aggregated for many users in organization). This id must always match user's
   * idBucket.
   * </p>
   * <p>
   * All client data must have idBucket assigned. Only it administrative data (like logs from logged-off users) may be
   * disconnected.
   * </p>
   */
  protected Integer idBucket;


  @Override
  public int compareTo(AbstractEntity entity) {
    return id.compareTo(entity.getId());
  }


  // getters & setters
  @Override
  public Integer getId() {
    return id;
  }
  @Override
  public void setId(Integer id) {
    this.id = id;
  }
  public Date getCreatedTime() {
    return createdTime;
  }
  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }
  public Integer getIdBucket() {
    return idBucket;
  }
  public void setIdBucket(Integer idBucket) {
    this.idBucket = idBucket;
  }
}

