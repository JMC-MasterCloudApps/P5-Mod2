package es.jmc.auth.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsServiceImplementation userDetailsService;

  @Value("${Authorization}")
  private String authorization;

  @Value("${Prefix}")
  private String prefix;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      authenticateJwt(request);

    } catch (Exception e) {
      log.error("Could not set user authentication.", e);
    }

    filterChain.doFilter(request, response);
  }

  private void authenticateJwt(HttpServletRequest request) {

    String jwt = parseJwt(request);

    if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
      return;
    }

    String username = jwtUtils.getUserNameFromJwtToken(jwt);
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    var authentication = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }


  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader(authorization);

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(prefix)) {
      return headerAuth.substring(7);
    }

    return null;
  }
}