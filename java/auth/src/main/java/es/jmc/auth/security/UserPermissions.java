package es.jmc.auth.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserPermissions {

  USER_READ("user:read"),
  USER_WRITE("user:write"),
  BOOK_READ("book:read"),
  BOOK_WRITE("book:write");

  private final String permission;

}
