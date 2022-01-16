package es.jmc.auth.security;

import static es.jmc.auth.security.UserPermissions.BOOK_READ;
import static es.jmc.auth.security.UserPermissions.BOOK_WRITE;
import static es.jmc.auth.security.UserPermissions.USER_READ;
import static es.jmc.auth.security.UserPermissions.USER_WRITE;

import com.google.common.collect.Sets;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

  USER(Sets.newHashSet()),
  ADMIN(Sets.newHashSet(USER_READ, USER_WRITE, BOOK_READ, BOOK_WRITE));

  private final Set<UserPermissions> permissions;
}
