package es.jmc.auth.controller;

import es.jmc.auth.model.Book;
import es.jmc.auth.model.Comment;
import es.jmc.auth.view.repository.BookRepository;
import es.jmc.auth.view.repository.CommentRepository;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookService {

	private final BookRepository books;
	private final CommentRepository comments;

	public Collection<Book> findAll() {
		return books.findAll();
	}

	public Optional<Book> findById(Long id) {
		return books.findById(id);
	}

	public void save(Book book) {
		books.save(book);
	}

	public void deleteById(Long id) {
		books.deleteById(id);
	}

	public void saveComment(Comment comment, Long bookId) {

		Book book = books.findById(bookId).orElseThrow();
		book.addComment(comment);

		comments.save(comment);
		books.save(book);
	}

	public void deleteCommentById(Long bookId, Long id) {

		Comment comment = comments.findById(id).orElseThrow();
		Book book = books.findById(bookId).orElseThrow();

		book.removeComment(comment);

		books.save(book);
		comments.delete(comment);
	}
}
