package es.jmc.auth.view.rest;

import java.util.List;

public record JwtResponse(String jwt, Long id, String username, String email, List<String> plainRoles) {
}
