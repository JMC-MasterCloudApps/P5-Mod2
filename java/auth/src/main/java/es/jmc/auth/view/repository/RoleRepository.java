package es.jmc.auth.view.repository;

import es.jmc.auth.model.ERole;
import es.jmc.auth.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(ERole name);

}