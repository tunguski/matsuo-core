package pl.matsuo.core.service.print;

import static com.lowagie.text.pdf.BaseFont.*;
import static java.util.Arrays.*;
import static org.xhtmlrenderer.resource.XMLResource.*;
import static pl.matsuo.core.util.function.FunctionalUtil.*;

import com.lowagie.text.DocumentException;
import freemarker.template.Configuration;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
public class PrintsRendererService implements IPrintsRendererService {
  private static final Logger logger = LoggerFactory.getLogger(PrintsRendererService.class);

  // Fonts used in flying saucer have to be specified here
  private static String[] FONTS = {"/font/verdana.ttf"};

  @Autowired protected Configuration freeMarkerConfiguration;

  private ITextRenderer initRenderer() throws DocumentException, IOException {
    ITextRenderer renderer = new ITextRenderer();

    asList(FONTS)
        .forEach(
            font ->
                runtimeEx(
                    () ->
                        renderer
                            .getFontResolver()
                            .addFont(
                                getClass().getResource(font).toURI().toString(),
                                IDENTITY_H,
                                NOT_EMBEDDED),
                    e -> logger.error("Error while configuring fonts", e)));

    return renderer;
  }

  @Override
  public byte[] renderHtml(String templateName, Object dataModel) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    runtimeEx(
        () ->
            freeMarkerConfiguration
                .getTemplate(templateName)
                .process(dataModel, new OutputStreamWriter(baos)));
    return baos.toByteArray();
  }

  @Override
  public byte[] createPDF(byte[] documentSource, String url) {
    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      ITextRenderer renderer = initRenderer();

      Document document = load(new ByteArrayInputStream(documentSource)).getDocument();
      renderer.setDocument(document, url);
      renderer.layout();
      renderer.createPDF(os);

      return os.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public byte[] generatePrint(String templateName, Object dataModel) {
    return runtimeEx(
        () -> {
          byte[] renderedFile = renderHtml(templateName, dataModel);
          return createPDF(
              renderedFile, getClass().getResource("/print/" + templateName).toURI().toString());
        });
  }

  public void setFreeMarkerConfiguration(Configuration freeMarkerConfiguration) {
    this.freeMarkerConfiguration = freeMarkerConfiguration;
  }
}
