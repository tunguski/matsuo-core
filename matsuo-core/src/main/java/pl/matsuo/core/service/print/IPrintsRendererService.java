package pl.matsuo.core.service.print;



public interface IPrintsRendererService {

  byte[] createPDF(byte[] document, String url);

  byte[] generatePrint(String templateName, Object dataModel);

  byte[] renderHtml(String templateName, Object dataModel);
}

