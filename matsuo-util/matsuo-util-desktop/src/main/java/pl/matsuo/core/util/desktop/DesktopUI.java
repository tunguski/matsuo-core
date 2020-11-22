package pl.matsuo.core.util.desktop;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static pl.matsuo.core.util.desktop.mvc.IRequest.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
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
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;
import pl.matsuo.core.util.desktop.mvc.IActionController;
import pl.matsuo.core.util.desktop.mvc.IActiveMonitor;
import pl.matsuo.core.util.desktop.mvc.IRequest;
import pl.matsuo.core.util.desktop.mvc.IView;

@Slf4j
public abstract class DesktopUI<M> extends Application {

  private final WebView webView = new WebView();
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Getter final DesktopUIData<M> data;
  Stage stage;
  String currentUrl;
  long lastPersistTime = System.currentTimeMillis();
  IView<IRequest, M> currentView;

  public DesktopUI(DesktopUIData<M> data) {
    this.data = data;

    maybeOverwriteModelFromFile();
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
                setupAfterPageLoad();
              }
            });
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  log.info("Save on shutdown");
                  maybePersistResult(null);
                }));

    updateView(request("/", emptyMap()));
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
    asList("a", "button")
        .forEach(
            type ->
                processElementsOfType(
                    doc, type, addEventListener("click", this::handleClickEvent, false)));

    if (currentView instanceof IActiveMonitor) {
      asList("input", "textarea", "select")
          .forEach(type -> processElementsOfType(doc, type, this::addInputHandler));
    }
  }

  private void addInputHandler(Element el) {
    addEventListener(
            "input",
            event -> {
              boolean modelChanged =
                  ((IActiveMonitor) currentView)
                      .onChange(el.getAttribute("name"), event, data.getModel());
              if (modelChanged) {
                maybePersistResult(currentView);
              }
            },
            false)
        .accept(el);
  }

  private Consumer<Element> addEventListener(
      String type, EventListener listener, boolean useCapture) {
    return el -> ((EventTarget) el).addEventListener(type, listener, useCapture);
  }

  private void processElementsOfType(Document doc, String type, Consumer<Element> handler) {
    NodeList links = doc.getElementsByTagName(type);
    for (int i = 0; i < links.getLength(); i++) {
      handler.accept((Element) links.item(i));
    }
  }

  void handleClickEvent(Event ev) {
    EventTarget target = ev.getTarget();
    Node node = (Node) target;
    processClick(node);
  }

  private void processClick(Node node) {
    if (node.getNodeName().equalsIgnoreCase("a")) {
      processLink(node);
    } else if (node.getNodeName().equalsIgnoreCase("button")
        || node.getNodeName().equalsIgnoreCase("input")) {
      processFormSubmit(node);
    } else {
      if (node.getParentNode() != null) {
        processClick(node.getParentNode());
      } else {
        throw new RuntimeException("Unknown event source");
      }
    }
  }

  private void processFormSubmit(Node node) {
    String action = findAction(node);
    Node form = findForm(node);
    Map<String, String> values = new HashMap<>();
    gatherFormValues(form, values);

    IRequest result = request(action, values);
    while (result != null) {
      if (data.getControllers().containsKey(result.getPath())) {
        IActionController<IRequest, M> actionController =
            data.getControllers().get(result.getPath());
        result = actionController.execute(result, data.model);

        maybePersistResult(actionController);
      } else {
        updateView(result);
        result = null;
      }
    }
  }

  private void gatherFormValues(Node node, Map<String, String> values) {
    if (node.getNodeName().equalsIgnoreCase("input")) {
      HTMLInputElement input = (HTMLInputElement) node;
      if (input.getName() != null && input.getValue() != null) {
        values.put(input.getName(), input.getValue());
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
    IRequest request = parseHref(href);
    updateView(request);
  }

  private void updateView(IRequest request) {
    Platform.runLater(
        () -> {
          String content = pageContent(request.getPath(), request.getParams());
          webView.getEngine().loadContent(content);
        });
  }

  private IRequest parseHref(String href) {
    if (href.contains("?")) {
      String[] split = href.split("[?]", 2);

      Map<String, String> params = new HashMap<>();

      for (String param : split[1].split("[&]")) {
        String[] paramSplit = param.split("[=]", 2);
        if (paramSplit.length == 1) {
          params.put(paramSplit[0], "");
        } else {
          params.put(paramSplit[0], paramSplit[1]);
        }
      }

      return request(split[0], params);
    } else {
      return request(href, emptyMap());
    }
  }

  private String pageContent(String path, Map<String, String> params) {
    if (data.views.containsKey(path)) {
      currentUrl = path;
      currentView = data.views.get(path);
      // return data.views.get(path).view(request(path, params), data.model).renderFormatted();
    } else if (data.views.containsKey("/404")) {
      currentUrl = "/404";
      currentView = data.views.get("/404");
      // view not found - render 404 view
      // return data.views.get("/404").view(request(path, params), data.model).renderFormatted();
    } else {
      currentUrl = null;
      return "Not found " + path;
    }

    return currentView.view(request(path, params), data.model).renderFormatted();
  }

  private synchronized void maybePersistResult(Object actionController) {
    if (actionController == null) {
      storeState();
      lastPersistTime = System.currentTimeMillis();
    } else {
      PersistResult persistResult = actionController.getClass().getAnnotation(PersistResult.class);
      if (persistResult != null) {
        if (persistResult.interval() <= 0
            || (lastPersistTime + (persistResult.interval() * 1000) < System.currentTimeMillis())) {
          storeState();
          lastPersistTime = System.currentTimeMillis();
        }
      }
    }
  }

  private void storeState() {
    try {
      File file = new File("." + getClass().getSimpleName());
      if (!file.exists()) {
        file.createNewFile();
      }

      try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
        FileChannel channel = fileOutputStream.getChannel();
        FileLock lock = channel.lock();
        // write to the channel
        channel.write(UTF_8.encode(gson.toJson(data.model)));
      }
    } catch (IOException e) {
      log.error("Exception while persisting state", e);
    }
  }

  private void maybeOverwriteModelFromFile() {
    try {
      File file = new File("." + getClass().getSimpleName());
      if (file.exists()) {
        String fileContent = FileUtils.readFileToString(file, UTF_8);
        M deserializedModel = (M) gson.fromJson(fileContent, data.model.getClass());
        data.model = deserializedModel;
      }
    } catch (IOException e) {
      log.error("Exception while reading state", e);
    }
  }
}
