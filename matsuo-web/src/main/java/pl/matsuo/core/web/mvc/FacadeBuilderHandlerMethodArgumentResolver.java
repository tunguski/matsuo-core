package pl.matsuo.core.web.mvc;

import static java.util.Arrays.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import pl.matsuo.core.params.IRequestParams;
import pl.matsuo.core.service.facade.FacadeBuilder;
import pl.matsuo.core.service.parameterprovider.IParameterProvider;
import pl.matsuo.core.service.parameterprovider.MapParameterProvider;

/**
 * Mapping request body to IRequestParams subinterfaces, allowing to use them in controller methods:
 *
 * <pre>
 * \@RequestMapping(value = "updateOwnPassword", method = PUT, consumes = { APPLICATION_JSON_VALUE })
 * \@ResponseStatus(NO_CONTENT)
 * public void updateOwnPassword(@RequestBody IChangePasswordParams changePasswordParams) {
 * </pre>
 *
 * If RequestBody annotation is present, parameters instance will be created on request's input
 * stream. If not, it will be created basing on request's params.
 *
 * <p>Created by tunguski on 23.11.13.
 */
@Component
public class FacadeBuilderHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  @Autowired FacadeBuilder facadeBuilder;
  protected Gson gson = new Gson();

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return IRequestParams.class.isAssignableFrom(parameter.getParameterType())
        || IParameterProvider.class.equals(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory)
      throws Exception {
    boolean returnFacade = IRequestParams.class.isAssignableFrom(parameter.getParameterType());
    Object facade;
    if (parameter.hasParameterAnnotation(RequestBody.class)) {
      String body =
          IOUtils.toString(webRequest.getNativeRequest(HttpServletRequest.class).getInputStream());
      if (returnFacade) {
        facade =
            facadeBuilder.createFacade(
                gson.fromJson(body, new TypeToken<Map<String, Object>>() {}.getType()),
                parameter.getParameterType());
      } else {
        facade =
            facadeBuilder.createParameterProvider(
                gson.fromJson(body, new TypeToken<Map<String, Object>>() {}.getType()));
      }
    } else {
      Map<String, String[]> parameterMap = webRequest.getParameterMap();
      Map<String, List<String>> params = new HashMap<>();
      for (String key : parameterMap.keySet()) {
        String[] values = parameterMap.get(key);
        params.put(key, new ArrayList<>(asList(values)));
      }

      IParameterProvider<?> parameterProvider =
          new MapParameterProvider(params) {
            @Override
            public Object internalGet(String key, Class<?> expectedClass) {
              if (expectedClass.equals(List.class)) {
                return super.internalGet(key, expectedClass);
              } else {
                Object list = super.internalGet(key, Object.class);
                if (list == null) {
                  return null;
                } else if (List.class.isAssignableFrom(list.getClass())) {
                  return ((List) list).get(0);
                } else {
                  return list;
                }
              }
            }
          };

      if (returnFacade) {
        facade = facadeBuilder.createFacade(parameterProvider, parameter.getParameterType());
      } else {
        facade = parameterProvider;
      }
    }

    String name = Conventions.getVariableNameForParameter(parameter);
    WebDataBinder binder = binderFactory.createBinder(webRequest, facade, name);
    validate(binder, parameter);

    mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());

    return facade;
  }

  private void validate(WebDataBinder binder, MethodParameter parameter) throws Exception {

    Annotation[] annotations = parameter.getParameterAnnotations();
    for (Annotation annot : annotations) {
      if (annot.annotationType().getSimpleName().startsWith("Valid")) {
        Object hints = AnnotationUtils.getValue(annot);
        binder.validate(hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});
        BindingResult bindingResult = binder.getBindingResult();
        if (bindingResult.hasErrors()) {
          if (isBindExceptionRequired(binder, parameter)) {
            throw new MethodArgumentNotValidException(parameter, bindingResult);
          }
        }
        break;
      }
    }
  }

  /**
   * Whether to raise a {@link MethodArgumentNotValidException} on validation errors.
   *
   * @param binder the data binder used to perform data binding
   * @param parameter the method argument
   * @return {@code true} if the next method argument is not of type {@link
   *     org.springframework.validation.Errors}.
   */
  private boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
    int i = parameter.getParameterIndex();
    Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
    boolean hasBindingResult =
        (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));

    return !hasBindingResult;
  }
}
