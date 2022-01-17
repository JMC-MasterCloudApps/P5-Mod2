package es.jmc.auth.model;

public enum ERole {

  ROLE_USER,
  ROLE_ADMIN;

  public static ERole find(String plainRole) {

    return "admin".equals(plainRole) ? ROLE_ADMIN : ROLE_USER;
  }
}
