package es.jmc.auth.model;

public class ResponseView {

  public interface Min {}
  public interface Basic extends Min {}
  public interface WithComment extends Basic {}
  public interface WithBook {}
  public interface WithUser {}
  public interface CommentWithBookId extends Min, WithBook, Basic {}
  public interface BookWithCommentsAndUser extends Basic, WithComment, WithUser {
  }
}
