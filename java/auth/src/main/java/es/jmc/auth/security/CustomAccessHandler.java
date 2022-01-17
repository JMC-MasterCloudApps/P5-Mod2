package es.jmc.auth.security;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAccessHandler implements AccessDeniedHandler {


  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
      throws IOException, ServletException {

    log.info("Forbidden access: {}", accessDeniedException.getMessage());

    response.setContentType(APPLICATION_JSON_VALUE);
    response.setStatus(SC_FORBIDDEN);

    final Map<String, Object> body = Map.of(
        "status", SC_FORBIDDEN,
        "error", "Forbidden",
        "message", accessDeniedException.getMessage(),
        "path", request.getServletPath()
    );

    final var mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), body);


  }
}
