package pl.matsuo.core.service;

import java.util.List;

public interface ListProvider {

  List<String> getElements(String query);

  List<String> getElements(String query, Integer size, Integer page);
}
