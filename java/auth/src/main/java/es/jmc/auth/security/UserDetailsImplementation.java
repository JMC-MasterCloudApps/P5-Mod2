package es.jmc.auth.security;


import com.fasterxml.jackson.annotation.JsonIgnore;
import es.jmc.auth.model.ERole;
import es.jmc.auth.model.Role;
import es.jmc.auth.model.User;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@AllArgsConstructor
public class UserDetailsImplementation implements UserDetails {

  private Long id;
  private String username;
  private String email;
  @JsonIgnore private String password;
  private Collection<? extends GrantedAuthority> authorities;

  public static UserDetailsImplementation build(User user) {

    List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
        .map(Role::getName)
        .map(ERole::name)
        .map(SimpleGrantedAuthority::new)
        .toList();

    return new UserDetailsImplementation(
        user.getId(),
        user.getNick(),
        user.getMail(),
        user.getPassword(),
        authorities);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
