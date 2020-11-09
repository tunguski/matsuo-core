package pl.matsuo.core.util.desktop;

import static java.util.Collections.emptyMap;
import static pl.matsuo.core.util.desktop.IRequest.request;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

@Slf4j
public abstract class DesktopUI extends Application {

  private final WebView webView = new WebView();

  final DesktopUIData data;
  Stage stage;

  public DesktopUI(DesktopUIData data) {
    this.data = data;
  }

  @Override
  public void start(Stage stage) {
    this.stage = stage;
    stage.setTitle("Home");
    WebEngine webEngine = webView.getEngine();
    webEngine.setJavaScriptEnabled(false);
    webEngine
        .getLoadWorker()
        .stateProperty()
        .addListener(
            (ov, oldState, newState) -> {
              if (newState == Worker.State.SUCCEEDED) {
                log.info("Page loaded");
                setupAfterPageLoad();
              } else {
                log.info("State changed " + newState);
              }
            });

    webEngine.loadContent(pageContent("/main"));
    BorderPane page = new BorderPane(webView);
    Scene scene = new Scene(page);
    stage.setScene(scene);
    stage.show();
  }

  private void setupAfterPageLoad() {
    Document doc = webView.getEngine().getDocument();

    // add click listener for anchor elements
    NodeList links = doc.getElementsByTagName("a");
    for (int i = 0; i < links.getLength(); i++) {
      log.info("Found link. Adding listen function.");
      Element el = (Element) links.item(i);
      ((EventTarget) el).addEventListener("click", this::handleEvent, false);
    }

    // update window title using page title
    NodeList titles = doc.getElementsByTagName("title");
    if (titles.getLength() > 0) {
      stage.setTitle(titles.item(0).getTextContent().trim());
    }
  }

  void handleEvent(Event ev) {
    EventTarget target = ev.getTarget();

    Node node = (Node) target;
    String href = node.getAttributes().getNamedItem("href").getTextContent();
    if (!href.contains("#")) {
      Platform.runLater(() -> webView.getEngine().loadContent(pageContent(href)));
    } else {
      String nextView = data.getControllers().get(href).execute(null);
      if (nextView != null) {
        Platform.runLater(() -> webView.getEngine().loadContent(pageContent(nextView)));
      }
    }
  }

  private String pageContent(String path) {
    if (data.views.containsKey(path)) {
      log.info("Rendering page " + path);
      return data.views.get(path).view(request(path, emptyMap())).renderFormatted();
    } else if (data.views.containsKey("/404")) {
      // view not found - render 404 view
      return data.views.get("/404").view(request(path, emptyMap())).renderFormatted();
    } else {
      return "Not found " + path;
    }
  }
}
