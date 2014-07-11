package pl.matsuo.core.model.message;

import org.hibernate.validator.constraints.NotEmpty;
import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.model.organization.AbstractParty;
import pl.matsuo.core.model.validation.EntityReference;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class AbstractMessage extends AbstractEntity {


    @EntityReference(value = AbstractParty.class)
    // FIXME: przy multiMessage brak ustawienia
    //@NotNull
    private Integer idParty;
    @NotEmpty
    @Column(columnDefinition = "clob")
    private String text;


    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Integer getIdParty() {
      return idParty;
    }
    public void setIdParty(Integer idParty) {
      this.idParty = idParty;
    }
}

