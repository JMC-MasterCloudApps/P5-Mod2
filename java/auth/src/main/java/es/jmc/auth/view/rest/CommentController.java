package es.jmc.auth.view.rest;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.jmc.auth.controller.BookService;
import es.jmc.auth.controller.CommentService;
import es.jmc.auth.controller.UserService;
import es.jmc.auth.model.Book;
import es.jmc.auth.model.Comment;
import es.jmc.auth.model.ResponseView;
import es.jmc.auth.security.UserDetailsImplementation;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentController {

	private final CommentService comments;
	private final UserService users;
	private final BookService books;

	@PostMapping("books/{id}/comments/")
	@JsonView(ResponseView.CommentWithBookId.class)
	public ResponseEntity<?> createComment(@RequestBody Comment comment, @PathVariable Long id) {

		final var authentication = SecurityContextHolder.getContext().getAuthentication();
		var userPrincipal = (UserDetailsImplementation) authentication.getPrincipal();
		String nick = userPrincipal.getUsername();

		if (nick == null) {
			return ResponseEntity.badRequest().body("User nick not provided");
		}

		comment.setUser(users.findByNick(nick).orElseThrow());

		books.saveComment(comment, id);

		URI location = fromCurrentRequest().path("/{id}").build(comment.getId());

		return ResponseEntity.created(location).body(comment);
	}

	@JsonView(ResponseView.CommentWithBookId.class)
	@DeleteMapping("books/{bookId}/comments/{id}")
	public Comment deleteComment(@PathVariable Long bookId, @PathVariable Long id) {

		Comment comment = comments.findById(id).orElseThrow();

		Comment commentToReturn = new Comment(comment);

		books.deleteCommentById(bookId, id);

		return commentToReturn;
	}

	@JsonView(ResponseView.CommentWithBookId.class)
	@GetMapping("books/{bookId}/comments/{id}")
	public Comment getComment(@PathVariable Long bookId, @PathVariable Long id) {

		return books.findById(bookId).orElseThrow()
				.getComments().stream().filter(comment -> comment.getId().equals(id)).findFirst().orElseThrow();

	}

	@GetMapping("/users/{id}/comments")
	@JsonView(ResponseView.CommentWithBookId.class)
	public List<Comment> getAllCommentsFromUser(@PathVariable long id) {

		if (!users.existsById(id)) {
			throw new NoSuchElementException();
		}

		return comments.findAllCommentsByUserId(id);
	}

}
