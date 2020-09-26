package pl.matsuo.core.web.test;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

@Slf4j
public class ViewTestUtil {

  public static final File uiRoot = new File("target/ui");

  public static void storeUiPage(String fileName, String content) {
    try {
      File file = new File(uiRoot, fileName);
      File folder = file.getParentFile();
      if (!folder.exists()) {
        log.info("Creating folder " + folder.getCanonicalPath());
        folder.mkdirs();
      }
      FileUtils.write(file, content, UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
