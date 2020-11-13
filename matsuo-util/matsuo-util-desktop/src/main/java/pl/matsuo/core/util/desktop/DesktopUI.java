package pl.matsuo.core.util.desktop;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static pl.matsuo.core.util.desktop.IRequest.request;

import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;

@Slf4j
public abstract class DesktopUI<M> extends Application {

  private final WebView webView = new WebView();

  @Getter final DesktopUIData<M> data;
  Stage stage;
  String currentUrl;

  public DesktopUI(DesktopUIData<M> data) {
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

    webEngine.loadContent(pageContent("/", emptyMap()));
    BorderPane page = new BorderPane(webView);
    Scene scene = new Scene(page);
    stage.setScene(scene);
    stage.show();
  }

  private void setupAfterPageLoad() {
    Document doc = webView.getEngine().getDocument();

    // add click listener for anchor elements
    addClickHandler(doc);

    // update window title using page title
    NodeList titles = doc.getElementsByTagName("title");
    if (titles.getLength() > 0) {
      stage.setTitle(titles.item(0).getTextContent().trim());
    }
  }

  private void addClickHandler(Document doc) {
    asList("a", "button").forEach(type -> addClickHandler(doc, type));
  }

  private void addClickHandler(Document doc, String type) {
    NodeList links = doc.getElementsByTagName(type);
    for (int i = 0; i < links.getLength(); i++) {
      log.info("Found link. Adding listen function.");
      Element el = (Element) links.item(i);
      ((EventTarget) el).addEventListener("click", this::handleEvent, false);
    }
  }

  void handleEvent(Event ev) {
    EventTarget target = ev.getTarget();
    Node node = (Node) target;
    log.info("Click " + node);
    if (node.getNodeName().equalsIgnoreCase("a")) {
      processLink(node);
    } else if (node.getNodeName().equalsIgnoreCase("button")
        || node.getNodeName().equalsIgnoreCase("input")) {
      processFormSubmit(node);
    } else {
      throw new RuntimeException("Unknown event source");
    }
  }

  private void processFormSubmit(Node node) {
    String action = findAction(node);
    Node form = findForm(node);
    Map<String, String> values = new HashMap<>();
    gatherFormValues(form, values);

    log.info("Processing form submit action: " + action + " params: " + values);

    IRequest result =
        data.getControllers().get(action).execute(request(action, values), data.model);
    while (result != null) {
      if (data.getControllers().containsKey(result.getPath())) {
        result = data.getControllers().get(result.getPath()).execute(result, data.model);
      } else {
        final IRequest getRequest = result;
        Platform.runLater(
            () ->
                webView
                    .getEngine()
                    .loadContent(pageContent(getRequest.getPath(), getRequest.getParams())));
        result = null;
      }
    }
  }

  private void gatherFormValues(Node node, Map<String, String> values) {
    if (node.getNodeName().equalsIgnoreCase("input")) {
      HTMLInputElement input = (HTMLInputElement) node;
      if (input.getName() != null && input.getValue() != null) {
        values.put(input.getName(), input.getValue());
      } else {
        log.info(
            "Found input but has no data; name: "
                + input.getName()
                + " value: "
                + input.getValue());
      }
    }

    for (int i = 0; i < node.getChildNodes().getLength(); i++) {
      gatherFormValues(node.getChildNodes().item(i), values);
    }
  }

  private Node findForm(Node node) {
    Node parentNode = node.getParentNode();
    while (parentNode != null) {
      if (parentNode.getNodeName().equalsIgnoreCase("form")) {
        return parentNode;
      } else {
        parentNode = parentNode.getParentNode();
      }
    }

    throw new RuntimeException("No form for node " + node);
  }

  private String findAction(Node node) {
    Node formaction = node.getAttributes().getNamedItem("formaction");
    if (formaction != null) {
      return formaction.getTextContent();
    }

    Node parentNode = node.getParentNode();
    while (parentNode != null) {
      if (parentNode.getNodeName().equalsIgnoreCase("form")) {
        Node action = parentNode.getAttributes().getNamedItem("action");
        if (action != null) {
          return action.getTextContent();
        } else {
          // form was found, but it has default action
          break;
        }
      }

      parentNode = parentNode.getParentNode();
    }

    return currentUrl;
  }

  private void processLink(Node node) {
    String href = node.getAttributes().getNamedItem("href").getTextContent();
    Platform.runLater(() -> webView.getEngine().loadContent(pageContent(href, emptyMap())));
  }

  private String pageContent(String path, Map<String, String> params) {
    if (data.views.containsKey(path)) {
      log.info("Rendering page " + path);
      currentUrl = path;
      return data.views.get(path).view(request(path, params), data.model).renderFormatted();
    } else if (data.views.containsKey("/404")) {
      currentUrl = "/404";
      // view not found - render 404 view
      return data.views.get("/404").view(request(path, params), data.model).renderFormatted();
    } else {
      currentUrl = null;
      return "Not found " + path;
    }
  }
}
