package es.jmc.auth.security;

import static es.jmc.auth.security.UserRole.ADMIN;
import static es.jmc.auth.security.UserRole.USER;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private static final String[] PUBLIC_PATHS = {"/", "index", "/css/*", "/js/*", "/api/auth/**"};
  private static final String[] BOOKS_PATH = {"/api/*/books/*"};
  private static final String[] COMMENTS_PATH = {"/api/*/books/*/comments/*", "/api/*/users/*/comments/*"};

  private final AuthEntryPointJwt unauthorizedHandler;

  private final UserDetailsServiceImplementation userDetailsService;

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {

    httpSecurity.cors().and().csrf().disable()
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeHttpRequests()
        .antMatchers(PUBLIC_PATHS).permitAll()
        .antMatchers(HttpMethod.GET, BOOKS_PATH).permitAll()
        .antMatchers(HttpMethod.POST, BOOKS_PATH).hasAnyRole(USER.name(), ADMIN.name())
        .antMatchers(HttpMethod.DELETE, BOOKS_PATH).hasAnyRole(USER.name(), ADMIN.name())
        .antMatchers(COMMENTS_PATH).hasAnyRole(USER.name(), ADMIN.name())
        .anyRequest().authenticated();
  }

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(new PasswordConfiguration().passwordEncoder());
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
