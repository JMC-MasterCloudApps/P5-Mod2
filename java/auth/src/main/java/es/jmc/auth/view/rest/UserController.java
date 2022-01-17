package es.jmc.auth.view.rest;

import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.jmc.auth.controller.CommentService;
import es.jmc.auth.controller.UserService;
import es.jmc.auth.model.Comment;
import es.jmc.auth.model.User;
import java.net.URI;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/users/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

	private final UserService users;
	private final CommentService comments;

	public ResponseEntity<?> createUser(@RequestBody User user) {

		try {

			users.save(user);

		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest()
					.body("User nick should be unique");
		}

		URI location = fromCurrentRequest().path("/{id}")
				.buildAndExpand(user.getId()).toUri();

		return ResponseEntity.created(location).body(user);
	}

	@PutMapping("{id}")
	public User replaceUser(@RequestBody User newUser, @PathVariable long id) {

		newUser.setId(id);
		users.replace(newUser);
		return newUser;
	}

	@GetMapping()
	public List<User> getUsers() {
		return users.findAll();
	}

	@GetMapping("{id}")
	public User getUser(@PathVariable long id) {
		return users.findById(id).orElseThrow();
	}

	@DeleteMapping("{id}")
	public ResponseEntity<User> deleteUser(@PathVariable long id) {

		User user = users.findById(id).orElseThrow();

		List<Comment> comment = comments.findAllCommentsByUserId(id);
		if (comment.isEmpty()) {
			users.deleteById(id);
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.status(PRECONDITION_FAILED).build();
		}
	}
}