package pl.matsuo.core.model.print;

import static javax.persistence.CascadeType.*;
import static javax.persistence.InheritanceType.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import pl.matsuo.core.model.kv.KeyValueEntity;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.model.validation.EntityReference;
import pl.matsuo.core.service.facade.IFacadeAware;

@Entity
@Inheritance(strategy = SINGLE_TABLE)
public class KeyValuePrint extends KeyValueEntity implements IFacadeAware {

  @NotNull private Class<? extends IPrintFacade> printClass;
  /** Reference to entity on which print is based. */
  private Integer idEntity;

  @EntityReference(User.class)
  private Integer idUserCreated;

  @OneToMany(cascade = ALL)
  @OrderColumn
  private List<KeyValuePrintElement> elements = new ArrayList<>();

  @Override
  @Transient
  @JsonIgnore
  public Class<? extends IPrintFacade> getPrintFacadeClass() {
    return printClass;
  }

  public static Supplier<? extends KeyValuePrint> print(
      Class<? extends IPrintFacade> clazz, Integer id) {
    return () -> printInitializer(clazz, id).apply(new KeyValuePrint());
  }

  public static <E extends KeyValuePrint> Function<E, E> printInitializer(
      Class<? extends IPrintFacade> clazz, Integer id) {
    return print -> {
      print.setPrintClass(clazz);
      print.setIdEntity(id);
      return print;
    };
  }

  public List<KeyValuePrintElement> getElements() {
    return elements;
  }

  public void setElements(List<KeyValuePrintElement> elements) {
    this.elements = elements;
  }

  public Class<? extends IPrintFacade> getPrintClass() {
    return printClass;
  }

  public void setPrintClass(Class<? extends IPrintFacade> printClass) {
    this.printClass = printClass;
  }
  /** Obecnie idAppointment. */
  public Integer getIdEntity() {
    return idEntity;
  }

  protected void setIdEntity(Integer idEntity) {
    this.idEntity = idEntity;
  }

  public Integer getIdUserCreated() {
    return idUserCreated;
  }

  public void setIdUserCreated(Integer idUserCreated) {
    this.idUserCreated = idUserCreated;
  }
}
