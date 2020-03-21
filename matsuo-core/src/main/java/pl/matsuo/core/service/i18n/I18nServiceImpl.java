package pl.matsuo.core.service.i18n;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

@Service
public class I18nServiceImpl implements I18nService {

  @Autowired MessageSource[] messageSources;

  @Override
  public String translate(String code, Object... params) {
    // FIXME: stworzyć localeProvidera
    Locale locale = Locale.getDefault();

    while (true) {
      for (MessageSource messageSource : messageSources) {
        try {
          return messageSource.getMessage(code, params, locale);
        } catch (NoSuchMessageException e) {
        }
      }

      if (locale == null) {
        return code;
      } else if (locale.toString().contains("_")) {
        locale = new Locale(locale.toString().substring(0, locale.toString().lastIndexOf("_")));
      } else if (!locale.toString().isEmpty()) {
        locale = null;
      }
    }
  }
}
