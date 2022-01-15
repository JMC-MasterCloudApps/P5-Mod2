package es.jmc.auth.view.repository;

import es.jmc.auth.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}