package pl.matsuo.core.service.permission;

import static java.lang.System.currentTimeMillis;
import static pl.matsuo.core.model.user.GroupEnum.ADMIN;

import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import pl.matsuo.core.service.permission.model.Permissions;
import pl.matsuo.core.service.session.SessionState;

@Service
public class PermissionService implements IPermissionService, ResourceLoaderAware {
  private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);

  @Autowired protected SessionState sessionState;
  private ResourceLoader resourceLoader;

  protected String permissionFilePath = "/permissions.json";
  protected Permissions permissions;
  protected long lastReadTime;
  protected long lastCheckTime;
  protected long interval = 2000;
  protected long authorizationLength = 30;

  protected Permissions getPermissions() {
    try {
      Resource resource = resourceLoader.getResource(permissionFilePath);

      if (permissions == null
          || ((lastCheckTime + interval < currentTimeMillis())
              && lastReadTime < resource.getFile().lastModified())) {
        logger.info("reading new permissions from: " + resource.getFile().getAbsolutePath());

        permissions =
            new Gson()
                .fromJson(new InputStreamReader(resource.getInputStream()), Permissions.class);
        lastReadTime = resource.getFile().lastModified();
        lastCheckTime = currentTimeMillis();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return permissions;
  }

  @Override
  public boolean isPermitted(String name) {
    return isPermitted(name, RequestType.GET);
  }

  @Override
  public boolean isPermitted(String name, RequestType requestType) {
    // weryfikacja czasu nieaktywności - po przekroczeniu automatycznie wylogowywuje
    if (authorizationLength > 0
        && sessionState.getUser() != null
        && (sessionState.getLastRequestTime() + authorizationLength * 60 * 1000)
            < currentTimeMillis()) {
      logoff();
    }

    // aktualizacja czasu ostatniego odwołania do serwisu
    // fixme: mechanizm wyłączeń dla cyklicznych zapytań nie będących wynikiem interakcji
    // użytkownika
    sessionState.setLastRequestTime(currentTimeMillis());

    // admin może wszystko
    if (sessionState.isInGroup(ADMIN.name())) {
      return true;
    } else if (requestType.equals(RequestType.OPTIONS)) {
      // na potrzeby CORS!
      return true;
    }

    // zbiór definicji funkcji pasujących do przekazanej nazwy
    Set<String> functionNames = functionDefinitions(name, requestType);
    // dodanie nazw zbiorów funkcji zawierających definicje matchujące nazwę
    Set<String> functionDefinitions = functionSets(functionNames, requestType);

    if (functionDefinitions.isEmpty()) {
      return false;
    }

    // weryfikacja nadania uprawnienia dla danego użytkownika i danej nazwy funkcji
    return findPermission(functionDefinitions);
  }

  @Override
  public void logoff() {
    sessionState.setUser(null);
  }

  /**
   * Czy któryś ze zbiorów funkcji uprawniających do wykonania akcji jest uprawniony dla którejś z
   * grup użytkownika.
   */
  protected boolean findPermission(Set<String> elements) {
    for (String groupName : getPermissions().getPermissions().keySet()) {
      if (sessionState.isInGroup(groupName)) {
        List<String> groupPermissions = getPermissions().getPermissions().get(groupName);
        for (String groupPermission : groupPermissions) {
          if (elements.contains(groupPermission)) {
            return true;
          }
        }
      }
    }

    return false;
  }

  /**
   * Tworzy zbiór zbiorów definicji funkcji, w których znajduje się dowolna z pasujących nazw
   * funkcji.
   */
  protected Set<String> functionSets(Set<String> functionNames, RequestType requestType) {
    Set<String> functionSets = new HashSet<>();

    for (String functionsSetName : getPermissions().getFunctionSets().keySet()) {
      Map<String, List<String>> functionSet =
          getPermissions().getFunctionSets().get(functionsSetName);
      for (String functionName : functionSet.keySet()) {
        if (functionNames.contains(functionName)
            && functionSet.get(functionName).contains(requestType.name())) {
          functionSets.add(functionsSetName);
        }
      }
    }

    return functionSets;
  }

  /** Czy dana nazwa pasuje do przekazanej funkcji (definicja uprawnienia). */
  protected boolean matches(String name, String function) {
    if (function.equals(name)) {
      return true;

      // zapis funkcji '/api/xxx/*' spowoduje automatyczne zezwolenie dla '/api/xxx', czyli każda
      // nazwa, włącznie z
      // brakiem slasha i czegokolwiek po xxx jest dozwolona
    } else if (function.endsWith("*")
        && name.indexOf(function.substring(0, function.length() - 2)) == 0) {
      if (function.length() <= 4) {
        throw new RuntimeException(
            "Wildcard definition must be longer than 4 characters: " + function);
      }

      return true;
    }

    return false;
  }

  /** Tworzy zbiór definicji funkcji, które pasują do przekazanego uprawnienia. */
  protected Set<String> functionDefinitions(String name, RequestType requestType) {
    Set<String> functionDefinitions = new HashSet<>();

    for (String function : getPermissions().getFunctions().keySet()) {
      if (matches(name, function)
          && getPermissions().getFunctions().get(function).contains(requestType.name())) {
        functionDefinitions.add(function);
      }
    }

    return functionDefinitions;
  }

  @Override
  public void setResourceLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }
}
