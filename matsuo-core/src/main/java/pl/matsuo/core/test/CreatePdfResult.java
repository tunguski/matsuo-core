package pl.matsuo.core.test;

import static java.util.Optional.ofNullable;
import static pl.matsuo.core.util.function.FunctionalUtil.runtimeEx;

import lombok.Getter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class CreatePdfResult {

  @Getter
  private String html;
  @Getter
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
                      PDDocument document = Loader.loadPDF(pdf);
                      PDFTextStripper stripper = new PDFTextStripper();

                      pdfString = stripper.getText(document);
                      return pdfString;
                    }));
  }
}
