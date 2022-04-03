package pl.matsuo.core.service.print;

import static java.util.Arrays.asList;
import static pl.matsuo.core.util.collection.CollectionUtil.forEach;
import static pl.matsuo.core.util.function.FunctionalUtil.processEx;
import static pl.matsuo.core.util.function.FunctionalUtil.runtimeEx;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.matsuo.core.service.template.ITemplateRegistry;
import pl.matsuo.core.util.function.ThrowingExceptionsSupplier;

@Slf4j
@Service
public class PrintsRendererService implements IPrintsRendererService {

  @Setter @Autowired ITemplateRegistry templateRegistry;

  // Fonts used in flying saucer have to be specified here
  private static String[] FONTS = {"/font/verdana.ttf"};

  private PdfRendererBuilder initRenderer() {
    PdfRendererBuilder builder = new PdfRendererBuilder();
    builder.useFastMode();

    forEach(
        asList(FONTS),
        font -> {
          InputStream fontStream =
              processEx(
                  (ThrowingExceptionsSupplier<InputStream>)
                      () -> getClass().getResource(font).openStream(),
                  e -> {
                    log.error("Error while configuring fonts", e);
                    throw new RuntimeException(e);
                  });
          builder.useFont(() -> fontStream, "verdana");
        });

    return builder;
  }

  @Override
  public String renderHtml(String templateName, Object dataModel) {
    return templateRegistry.getTemplate(templateName).generate(dataModel);
  }

  @Override
  public byte[] createPDF(String documentSource, String url) {
    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {

      PdfRendererBuilder builder = initRenderer();
      builder.withHtmlContent(documentSource, null);
      builder.toStream(os);
      builder.run();

      return os.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public byte[] generatePrint(String templateName, Object dataModel) {
    return runtimeEx(() -> createPDF(renderHtml(templateName, dataModel), null));
  }
}
