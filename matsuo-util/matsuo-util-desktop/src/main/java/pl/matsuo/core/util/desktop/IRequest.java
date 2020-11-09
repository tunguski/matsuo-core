package pl.matsuo.core.util.desktop;

import java.util.Map;

public interface IRequest {

  String getPath();

  Map<String, String> getParams();

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
