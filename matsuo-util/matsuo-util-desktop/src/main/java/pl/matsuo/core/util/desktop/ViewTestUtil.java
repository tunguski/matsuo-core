package pl.matsuo.core.util.desktop;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class ViewTestUtil {

  public static void storeView(String name, String content) {
    File viewDir = new File("target/views");
    viewDir.mkdirs();
    File file = new File(viewDir, name);

    try {
      FileUtils.write(file, content, UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
