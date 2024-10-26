package pl.matsuo.core.model.print;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.InheritanceType.SINGLE_TABLE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pl.matsuo.core.model.kv.KeyValueEntity;
import pl.matsuo.core.model.user.User;
import pl.matsuo.core.model.validation.EntityReference;
import pl.matsuo.core.service.facade.IFacadeAware;

@Entity
@Inheritance(strategy = SINGLE_TABLE)
@Getter
@Setter
public class KeyValuePrint extends KeyValueEntity implements IFacadeAware {

  @NotNull private Class<? extends IPrintFacade> printClass;

  /** Reference to entity on which print is based. */
  private Long idEntity;

  @EntityReference(User.class)
  private Long idUserCreated;

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
      Class<? extends IPrintFacade> clazz, Long id) {
    return () -> printInitializer(clazz, id).apply(new KeyValuePrint());
  }

  public static <E extends KeyValuePrint> Function<E, E> printInitializer(
      Class<? extends IPrintFacade> clazz, Long id) {
    return print -> {
      print.setPrintClass(clazz);
      print.setIdEntity(id);
      return print;
    };
  }
}
