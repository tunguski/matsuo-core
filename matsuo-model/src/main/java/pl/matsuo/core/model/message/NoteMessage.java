package pl.matsuo.core.model.message;

import static pl.matsuo.core.model.message.NoteStatus.*;

import javax.persistence.Entity;

/** Created by marek on 07.07.14. */
@Entity
public class NoteMessage extends AbstractMessage {

  private NotePriority priority;
  private NoteStatus status = OPEN;

  public NotePriority getPriority() {
    return priority;
  }

  public void setPriority(NotePriority priority) {
    this.priority = priority;
  }

  public NoteStatus getStatus() {
    return status;
  }

  public void setStatus(NoteStatus status) {
    this.status = status;
  }
}
