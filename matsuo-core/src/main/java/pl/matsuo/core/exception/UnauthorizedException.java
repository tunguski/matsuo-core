package pl.matsuo.core.exception;


import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

/**
 * Created by marek on 12.07.14.
 */
@ResponseStatus(UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
  private static final long serialVersionUID = 1L;
}

