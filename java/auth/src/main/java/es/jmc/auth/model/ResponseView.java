package es.jmc.auth.model;

public class ResponseView {

  public interface Min {}
  public interface Basic extends Min {}
  public interface WithComment extends Basic {}
  public interface WithBook extends Min {}
  public interface WithUser {}
  public interface CommentWithBookId extends Basic, WithBook  {}
  public interface BookWithCommentsAndUser extends Basic, WithComment, WithUser {}

  public interface Full extends Basic, BookWithCommentsAndUser, CommentWithBookId {}
}
