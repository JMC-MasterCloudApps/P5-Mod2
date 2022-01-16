package es.jmc.auth.security;

import static es.jmc.auth.security.UserRole.ADMIN;
import static es.jmc.auth.security.UserRole.USER;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private static final String[] PUBLIC_PATHS = {"/", "index", "/css/*", "/js/*"};
  private static final String[] API_PATHS = {"/api/**"};


  private final PasswordEncoder passwordEncoder;

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {

    httpSecurity
        .csrf().disable()
        .authorizeHttpRequests()
        .antMatchers(PUBLIC_PATHS).permitAll()
        .antMatchers(API_PATHS).hasRole(ADMIN.name())
        .and()
        .httpBasic();
}

  @Bean
  @Override
  protected UserDetailsService userDetailsService() {

    final var bob = User.builder()
        .username("bob")
        .password(passwordEncoder.encode("clean"))
        .roles(ADMIN.name())
//        .authorities()
        .build();

    final var jim = User.builder()
        .username("jim")
        .password(passwordEncoder.encode("jimbo"))
        .roles(USER.name())
//        .authorities()
        .build();


    return new InMemoryUserDetailsManager(bob, jim);
  }

}
