package es.jmc.auth.security;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {


  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException {

    log.info("Unauthorized error: {}", authException.getMessage());

    response.setContentType(APPLICATION_JSON_VALUE);
    response.setStatus(SC_UNAUTHORIZED);

    final Map<String, Object> body = Map.of(
        "status", SC_UNAUTHORIZED,
        "error", "Unauthorized",
        "message", authException.getMessage(),
        "path", request.getServletPath()
    );

    final var mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), body);
  }
}
