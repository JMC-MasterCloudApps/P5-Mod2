package es.jmc.auth.view.rest;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.jmc.auth.controller.BookService;
import es.jmc.auth.model.Book;
import es.jmc.auth.model.Comment;
import es.jmc.auth.model.ResponseView;
import es.jmc.auth.model.User;
import java.net.URI;
import java.util.Collection;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("api/v1/books/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookController {

	private final BookService books;

	@GetMapping()
	public Collection<Book> getBooks() {
		return books.findAll();
	}

	@GetMapping("{id}")
	@JsonView(ResponseView.BookWithCommentsAndUser.class)
	public Book getBook(@PathVariable long id) {

		return books.findById(id).orElseThrow();
	}

	@PostMapping()
	public ResponseEntity<Book> createBook(@RequestBody Book book) {

		books.save(book);

		URI location = fromCurrentRequest().path("/{id}")
				.buildAndExpand(book.getId()).toUri();

		return ResponseEntity.created(location).body(book);
	}

}