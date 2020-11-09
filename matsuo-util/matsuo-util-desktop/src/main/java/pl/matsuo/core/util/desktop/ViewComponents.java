package pl.matsuo.core.util.desktop;

import static j2html.TagCreator.attrs;
import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.rawHtml;
import static j2html.TagCreator.style;
import static j2html.TagCreator.tbody;
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
import org.apache.commons.io.IOUtils;

public class ViewComponents {

  String bootstrapCss;
  String customCss;

  {
    try {
      customCss = IOUtils.toString(getClass().getResourceAsStream("/css/custom.css"), UTF_8);
      bootstrapCss =
          IOUtils.toString(getClass().getResourceAsStream("/css/bootstrap-4.5.3.css"), UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public ContainerTag pageTemplate(String title, DomContent... bodyContent) {
    return html(
        head(
            title(title),
            style(rawHtml(bootstrapCss)).attr("type", "text/css"),
            style(rawHtml(customCss)).attr("type", "text/css")),
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
}
