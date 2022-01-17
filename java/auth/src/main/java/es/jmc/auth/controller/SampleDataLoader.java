package es.jmc.auth.controller;

import es.jmc.auth.model.Book;
import es.jmc.auth.model.Comment;
import es.jmc.auth.model.ERole;
import es.jmc.auth.model.Role;
import es.jmc.auth.model.User;
import es.jmc.auth.view.repository.BookRepository;
import es.jmc.auth.view.repository.RoleRepository;
import es.jmc.auth.view.repository.UserRepository;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SampleDataLoader {

	private final BookRepository books;
	private final UserRepository users;
	private final RoleRepository roles;
	private final PasswordEncoder passEncoder;

	@PostConstruct
	public void init() {

		final var adminRole = roles.save(new Role(ERole.ROLE_ADMIN));
		final var userRole = roles.save(new Role(ERole.ROLE_USER));

		var user1 = users.save(new User("pepe", "pepe@gmail.com", passEncoder.encode("pass"), Set.of(adminRole)));
		var user2 = users.save(new User("juan", "juan@hotmail.com", passEncoder.encode("pass"), Set.of(userRole)));
		users.save(new User("rafa", "rafa85@terra.es", passEncoder.encode("pass"), Set.of(userRole)));
		
		var book1 = new Book(
				"Don Quijote",
				"En un lugar de la Mancha",
				"Cervantes",
				"Desconocido",
				1605);

		book1.addComment(new Comment(user1, 5, "un clásico"));
		book1.addComment(new Comment(user2, 0, "no me ha gustado"));
		
		var book2 = new Book(
				"El principito",
				"Un piloto se pierde en el Sáhara",
				"Antoine de Saint-Exupéry",
				"Gallimard",
				1943);
		
		book2.addComment(new Comment(user2, 0, "este tampoco"));
		
		var book3 = new Book(
				"Lazarillo de Tormes",
				"La vida de Lazarillo de Tormes y de sus fortunas y adversidades",
				"Desconocido",
				"Acceso público",
				1554);
		
		books.save(book1);		
		books.save(book2);
		books.save(book3);
	}

}
