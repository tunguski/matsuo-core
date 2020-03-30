package pl.matsuo.core.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
  private static final long serialVersionUID = 1L;
}
