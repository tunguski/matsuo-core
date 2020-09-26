package pl.matsuo.core.service.print;

import static com.lowagie.text.pdf.BaseFont.IDENTITY_H;
import static com.lowagie.text.pdf.BaseFont.NOT_EMBEDDED;
import static java.util.Arrays.asList;
import static org.xhtmlrenderer.resource.XMLResource.load;
import static pl.matsuo.core.util.function.FunctionalUtil.runtimeEx;

import freemarker.template.Configuration;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Slf4j
@Service
public class PrintsRendererService implements IPrintsRendererService {

  // Fonts used in flying saucer have to be specified here
  private static String[] FONTS = {"/font/verdana.ttf"};

  @Autowired protected Configuration freeMarkerConfiguration;

  private ITextRenderer initRenderer() {
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
                    e -> log.error("Error while configuring fonts", e)));

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
