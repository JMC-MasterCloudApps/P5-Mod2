package es.jmc.auth.view.rest;

import java.util.Set;

public record SignupRequest(String username, String email, String password, Set<String> roles) { }
