package pl.matsuo.core.test;

import static freemarker.template.Configuration.VERSION_2_3_30;
import static freemarker.template.TemplateExceptionHandler.HTML_DEBUG_HANDLER;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.asList;
import static java.util.Collections.reverse;
import static org.apache.commons.io.IOUtils.write;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.matsuo.core.conf.GeneralConfig;
import pl.matsuo.core.service.facade.FacadeBuilder;
import pl.matsuo.core.service.facade.IFacadeBuilder;
import pl.matsuo.core.service.i18n.I18nServiceImpl;
import pl.matsuo.core.service.print.PrintMethods;
import pl.matsuo.core.service.print.PrintsRendererService;
import pl.matsuo.core.test.data.TestSessionState;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    classes = {
      GeneralConfig.class,
      FacadeBuilder.class,
      I18nServiceImpl.class,
      TestSessionState.class
    })
abstract class AbstractPrintGeneratingTest<E> implements PrintMethods {

  @Autowired protected FacadeBuilder facadeBuilder;

  protected Configuration freeMarkerConfiguration;
  protected PrintsRendererService printsRendererService = new PrintsRendererService();
  protected String templateDirectory = "./src/main/resources/";
  protected File targetDirectory = new File("./target/test-prints/");

  AbstractPrintGeneratingTest() {
    targetDirectory.mkdirs();

    try {
      freeMarkerConfiguration = new Configuration(VERSION_2_3_30);
      freeMarkerConfiguration.setTemplateLoader(new ClassTemplateLoader(Class.class, "/"));
      freeMarkerConfiguration.setDirectoryForTemplateLoading(
          new File(templateDirectory).getAbsoluteFile());
      freeMarkerConfiguration.setObjectWrapper(
          new DefaultObjectWrapperBuilder(VERSION_2_3_30).build());
      freeMarkerConfiguration.setDefaultEncoding("UTF-8");
      freeMarkerConfiguration.setTemplateExceptionHandler(HTML_DEBUG_HANDLER);
      freeMarkerConfiguration.setIncompatibleImprovements(VERSION_2_3_30);

      printsRendererService.setFreeMarkerConfiguration(freeMarkerConfiguration);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /** @return Zwraca parę - wyrenderowany html i binarny pdf. */
  CreatePdfResult testCreatePDF(String htmlInputPath, Map<String, Object> dataModel)
      throws Exception {
    // w poszczególnych testach nie trzeba ustawiać formatu
    dataModel.put("outputFormat", "pdf");

    File outputFile =
        new File(
            targetDirectory,
            htmlInputPath
                .substring(htmlInputPath.lastIndexOf("/") + 1)
                .replace(".ftl", "-" + lookupTestName() + ".pdf"));

    try (FileOutputStream fos = new FileOutputStream(outputFile)) {
      assertNotNull("Cold not find resource!", getClass().getResource(htmlInputPath));

      byte[] renderedFile = printsRendererService.renderHtml(htmlInputPath, dataModel);

      assertNotNull("File not found: renderedFile is null!", renderedFile);

      byte[] pdf = printsRendererService.createPDF(renderedFile, getResourceUrl(htmlInputPath));

      assertTrue("Created PDF size is 0!", pdf.length > 0);

      write(pdf, fos);

      return new CreatePdfResult(new String(renderedFile), pdf);
    }
  }

  /** Create print data model from object passed. */
  protected abstract Map<String, Object> buildModel(E params);

  /** Generate test print and validate it with verifications. */
  protected CreatePdfResult testCreatePDF(E params, BiConsumer<String, String>... verifications)
      throws Exception {
    CreatePdfResult pdfResult = testCreatePDF(getPrintFileName(), buildModel(params));

    for (BiConsumer<String, String> verification : verifications) {
      verification.accept(pdfResult.getHtml(), pdfResult.getPdfString());
    }

    return pdfResult;
  }

  protected String lookupTestName() {
    List<StackTraceElement> asList = asList(currentThread().getStackTrace());
    reverse(asList);

    for (StackTraceElement stackTraceElement : asList) {
      String className = stackTraceElement.getClassName();
      String methodName = stackTraceElement.getMethodName();

      try {
        getClass();
        Class<?> clazz = Class.forName(className);
        Method method = clazz.getMethod(methodName);

        if (method.isAnnotationPresent(Test.class)) {
          return method.getName();
        }
      } catch (Exception e) {
        // no method found, looking deeper
      }
    }

    throw new IllegalStateException("No method found annotated with org.junit.Test");
  }

  private String getResourceUrl(String path) throws URISyntaxException {
    return getClass().getResource(path).toURI().toString();
  }

  protected abstract String getPrintFileName();

  public IFacadeBuilder getFacadeBuilder() {
    return facadeBuilder;
  }
}
