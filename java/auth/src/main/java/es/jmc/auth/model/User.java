package es.jmc.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

@Data
@Entity
@Table(name = "User", uniqueConstraints = {
		@UniqueConstraint(columnNames = "nick"),
		@UniqueConstraint(columnNames = "mail")
})
@NoArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(ResponseView.Basic.class)
	private Long id;

	@Column(unique = true)
	@JsonView(ResponseView.Basic.class)
	@NotBlank
	@Size(max = 20)
	private String nick;

	@JsonView(ResponseView.Basic.class)
	@NotBlank
	@Size(max = 50)
	@Email
	private String mail;

	@JsonIgnore
	@NotBlank
	@Size(max = 120)
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public User(String nick, String mail) {
		this.nick = nick;
		this.mail = mail;
	}

	public User(String username, String email, String password) {
		nick = username;
		mail = email;
		this.password = password;
	}

	public User(String username, String email, String password, Set<Role> roles) {
		nick = username;
		mail = email;
		this.password = password;
		this.roles = roles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		User user = (User) o;
		return id != null && Objects.equals(id, user.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
