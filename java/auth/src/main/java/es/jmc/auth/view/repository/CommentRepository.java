package es.jmc.auth.view.repository;

import es.jmc.auth.model.Comment;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query("SELECT DISTINCT p FROM Comment p JOIN p.user u WHERE u.id=?1")
	List<Comment> findAllCommentsByUserId(Long userId);

}