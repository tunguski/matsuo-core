package pl.matsuo.core.service.print;

public interface IPrintsRendererService {

  byte[] createPDF(String document, String url);

  byte[] generatePrint(String templateName, Object dataModel);

  String renderHtml(String templateName, Object dataModel);
}
