package pl.matsuo.core.util.desktop.mvc;

import java.util.Map;

public interface IRequest {

  String getPath();

  Map<String, String> getParams();

  default boolean hasParam(String name) {
    return getParams().containsKey(name);
  }

  default String getParam(String name) {
    return getParams().get(name);
  }

  static IRequest request(String path, Map<String, String> params) {
    return new IRequest() {
      @Override
      public String getPath() {
        return path;
      }

      @Override
      public Map<String, String> getParams() {
        return params;
      }
    };
  }
}
