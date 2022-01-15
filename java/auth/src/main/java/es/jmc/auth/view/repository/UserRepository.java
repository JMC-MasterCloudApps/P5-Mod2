package es.jmc.auth.view.repository;

import es.jmc.auth.model.User;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByNick(String nick);

}