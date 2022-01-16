package es.jmc.auth.model;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(ResponseView.Min.class)
	private Long id;

	@JsonView(ResponseView.Min.class)
	private String title;

	@Column(columnDefinition = "TEXT")
	@JsonView(ResponseView.Basic.class)
	private String summary;

	@JsonView(ResponseView.Basic.class)
	private String author;

	@JsonView(ResponseView.Basic.class)
	private String editorial;

	@JsonView(ResponseView.Basic.class)
	private int publishYear;

	@JsonView(ResponseView.WithComment.class)
	@OneToMany(mappedBy="book", cascade = CascadeType.ALL, orphanRemoval = true)
	@ToString.Exclude
	private List<Comment> comments;

	public Book(String title, String summary, String author, String editorial, int publishYear) {
		this(title, summary, author, editorial, publishYear, new ArrayList<>());
	}
	
	public Book(String title, String summary, String author, String editorial, int publishYear, List<Comment> comments) {
		this.title = title;
		this.summary = summary;
		this.author = author;
		this.editorial = editorial;
		this.publishYear = publishYear;		
		this.comments = comments;
	}

	public void addComment(Comment comment) {
		comments.add(comment);
		comment.setBook(this);
	}
	
	public void removeComment(Comment comment) {
		boolean removed = comments.remove(comment);
		if(!removed) {
			throw new NoSuchElementException();
		}
		comment.setBook(null);
	}

	@Override
	public String toString() {
		return String.format("Book [id=%s, title=%s, summary=%s, author=%s, editorial=%s, publishYear=%d", id, title,
				summary, author, editorial, publishYear);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		Book book = (Book) o;
		return id != null && Objects.equals(id, book.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
