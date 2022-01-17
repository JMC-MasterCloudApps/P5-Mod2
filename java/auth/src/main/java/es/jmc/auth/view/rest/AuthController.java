package es.jmc.auth.view.rest;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toSet;

import es.jmc.auth.model.ERole;
import es.jmc.auth.model.Role;
import es.jmc.auth.model.User;
import es.jmc.auth.security.JwtUtils;
import es.jmc.auth.security.UserDetailsImplementation;
import es.jmc.auth.view.repository.RoleRepository;
import es.jmc.auth.view.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/auth/")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

  private final AuthenticationManager authManager;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passEncoder;
  private final JwtUtils jwtUtils;

  @PostMapping("signup")
  public ResponseEntity<String> registerUser(@Valid @RequestBody SignupRequest request) {

    final var validationMessage = checkUserAndEmail(request);
    if (validationMessage.isPresent()) {
      return ResponseEntity.badRequest().body(validationMessage.get());
    }

    var user = buildUser(request);
    userRepository.save(user);

    return ResponseEntity.ok("User registered successfully!");
  }

  private Optional<String> checkUserAndEmail(SignupRequest request) {

    if (userRepository.existsByNick(request.username())) {
      return of("Username already exists!");
    }

    if (userRepository.existsByMail(request.email())) {
      return of("Email already exists!");
    }

    return empty();
  }

  private User buildUser(SignupRequest request) {

    var username = request.username();
    var email = request.email();
    var password = passEncoder.encode(request.password());
    var roles = getRoles(request.roles());

    return new User(username, email, password, roles);
  }

  private Set<Role> getRoles(Set<String> plainRoles) {

    Set<Role> roles = plainRoles.stream()
        .map(ERole::find)
        .map(roleRepository::findByName)
        .flatMap(Optional::stream)
        .collect(toSet());

    if (roles.isEmpty()) {
      log.error("Error: No roles found: {}", String.join(", ", plainRoles));
      throw new RuntimeException("Error: No roles found!");
    }

    return roles;
  }

  @PostMapping("signin")
  public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {

    log.info(request.toString());
    var userPassToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
    Authentication authentication = authManager.authenticate(userPassToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    return ResponseEntity.ok(buildJwtResponse(authentication));
  }

  private JwtResponse buildJwtResponse(Authentication authentication) {

    String jwt = jwtUtils.generateJwtToken(authentication);
    final var userDetails = (UserDetailsImplementation) authentication.getPrincipal();
    List<String> plainRoles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();

    return new JwtResponse(
        jwt,
        userDetails.getId(),
        userDetails.getUsername(),
        userDetails.getEmail(),
        plainRoles);
  }
}
