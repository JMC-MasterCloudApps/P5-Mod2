package es.jmc.auth.controller;

import es.jmc.auth.model.Comment;
import es.jmc.auth.view.repository.CommentRepository;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentService {

	private final CommentRepository comments;

	public Optional<Comment> findById(long id) {
		return comments.findById(id);
	}

	public void save(Comment comment) {
		this.comments.save(comment);
	}

	public void delete(Comment comment) {
		this.comments.delete(comment);
	}

	public List<Comment> findAllCommentsByUserId(Long userId) {
		return comments.findAllCommentsByUserId(userId);
	}
	
}
