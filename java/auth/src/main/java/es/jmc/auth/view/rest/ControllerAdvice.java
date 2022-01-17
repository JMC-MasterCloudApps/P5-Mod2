package es.jmc.auth.view.rest;

import static es.jmc.auth.security.UserRole.ADMIN;
import static es.jmc.auth.security.UserRole.USER;

import es.jmc.auth.model.Book;
import es.jmc.auth.model.ResponseView;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

@Slf4j
@ControllerAdvice
class JsonViewConfiguration extends AbstractMappingJacksonResponseBodyAdvice {

  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    return super.supports(returnType, converterType);
  }

  @Override
  protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer,
                                         MediaType contentType,
                                         MethodParameter returnType,
                                         ServerHttpRequest request,
                                         ServerHttpResponse response) {

    Class<?> viewClass = ResponseView.Min.class;
    final var authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || authentication.getAuthorities() == null) {
      bodyContainer.setSerializationView(viewClass);
      return;
    }

    final var authorities = authentication.getAuthorities();

    if (authorities.stream()
        .anyMatch(o -> USER.getGrantedAuthorities().contains(o))) {
      viewClass = ResponseView.WithComment.class;
    }

    if (authorities.stream()
        .anyMatch(o -> ADMIN.getGrantedAuthorities().contains(o))) {

      viewClass = ResponseView.WithComment.class;
    }

    log.info("Response view: {}", viewClass.getSimpleName());
    bodyContainer.setSerializationView(viewClass);
  }
}