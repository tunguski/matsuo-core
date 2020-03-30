package pl.matsuo.core.test;

import static java.util.Optional.ofNullable;
import static pl.matsuo.core.util.function.FunctionalUtil.*;

import java.io.ByteArrayInputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class CreatePdfResult {

  private String html;
  private byte[] pdf;
  private String pdfString;

  public CreatePdfResult(String html, byte[] pdf) {
    this.html = html;
    this.pdf = pdf;
  }

  public String getPdfString() {
    return ofNullable(pdfString)
        .orElseGet(
            () ->
                runtimeEx(
                    () -> {
                      PDDocument document = PDDocument.load(new ByteArrayInputStream(pdf));
                      PDFTextStripper stripper = new PDFTextStripper();

                      pdfString = stripper.getText(document);
                      return pdfString;
                    }));
  }

  public String getHtml() {
    return html;
  }

  public byte[] getPdf() {
    return pdf;
  }
}
