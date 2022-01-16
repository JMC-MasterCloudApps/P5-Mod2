package es.jmc.auth.security;

import static es.jmc.auth.security.UserPermissions.BOOK_READ;
import static es.jmc.auth.security.UserPermissions.BOOK_WRITE;
import static es.jmc.auth.security.UserPermissions.USER_READ;
import static es.jmc.auth.security.UserPermissions.USER_WRITE;
import static java.util.stream.Collectors.toSet;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum UserRole {

  USER(Sets.newHashSet(BOOK_READ, BOOK_WRITE)),
  ADMIN(Sets.newHashSet(USER_READ, USER_WRITE, BOOK_READ, BOOK_WRITE));

  private final Set<UserPermissions> permissions;


  public Set<SimpleGrantedAuthority> getGrantedAuthorities() {

    Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
        .map(UserPermissions::getPermission)
        .map(SimpleGrantedAuthority::new)
        .collect(toSet());

    permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

    return permissions;
  }
}
