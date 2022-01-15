package es.jmc.auth.model;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Comment {

	public interface Basic {}
	public interface WithBook {}
	public interface WithUser {}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(Basic.class)
	private Long id;

	@Column(columnDefinition = "TEXT")
	@JsonView(Basic.class)
	private String publishtext;

	@JsonView(Basic.class)
	private int punctuation;
	
	@ManyToOne
	@JsonView(WithBook.class)
	private Book book;
	
	@ManyToOne
	@JsonView(WithUser.class)
	private User user;

	public Comment(User user, int punctuation, String publishtext) {

		this.user = user;
		this.publishtext = publishtext;
		this.punctuation = punctuation;
	}

	public Comment(Comment comment) {
		this.id = comment.id;
		this.user = comment.user;
		this.publishtext = comment.publishtext;
		this.punctuation = comment.punctuation;
		this.book = comment.book;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		Comment comment = (Comment) o;
		return id != null && Objects.equals(id, comment.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
