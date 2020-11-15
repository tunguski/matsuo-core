package pl.matsuo.core.util.desktop.component;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.rawHtml;
import static j2html.TagCreator.style;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.text;
import static j2html.TagCreator.thead;
import static j2html.TagCreator.title;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.stream;

import j2html.TagCreator;
import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import pl.matsuo.core.util.desktop.IRequest;

@NoArgsConstructor
public class ViewComponents {

  String bootstrapCss = loadResource(ViewComponents.class, "/css/custom.css");
  String customCss = loadResource(ViewComponents.class, "/css/bootstrap-4.5.3.css");
  String additionalCss = "";

  public ViewComponents(String additionalCss) {
    this.additionalCss = additionalCss;
  }

  public static String loadResource(Class<?> clazz, String path) {
    try {
      return IOUtils.toString(clazz.getResourceAsStream(path), UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public ContainerTag pageTemplate(String title, DomContent... bodyContent) {
    return html(
        head(
            title(title),
            style(rawHtml(bootstrapCss)).attr("type", "text/css"),
            style(rawHtml(customCss)).attr("type", "text/css"),
            style(rawHtml(additionalCss)).attr("type", "text/css")),
        body(div(attrs(".container-fluid"), each(stream(bodyContent)))));
  }

  public DomContent rowCol(DomContent... content) {
    return div(attrs(".row"), div(attrs(".col"), each(stream(content))));
  }

  public <E> DomContent table(
      DomContent headerRow, List<E> rows, Function<? super E, DomContent> mapper) {
    return TagCreator.table(
        attrs(".table.table-sm.table-hover"), thead(headerRow), tbody(each(rows, mapper)));
  }

  public DomContent maybeShowAlert(IRequest request, String param, AlertType type) {
    if (request.hasParam(param)) {
      return div(attrs(".alert.alert-" + type.name()), text(request.getParam(param)));
    } else {
      return null;
    }
  }
}
