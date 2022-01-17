package es.jmc.auth.view.rest;

import com.fasterxml.jackson.annotation.JsonView;
import es.jmc.auth.model.ResponseView;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {

  @JsonView(ResponseView.Basic.class)
  private String jwt;

  @JsonView(ResponseView.Basic.class)
  private static final String type = "Bearer";

  @JsonView(ResponseView.Basic.class)
  private Long id;

  @JsonView(ResponseView.Basic.class)
  private String username;

  @JsonView(ResponseView.Basic.class)
  private String email;

  @JsonView(ResponseView.Basic.class)
  private List<String> plainRoles;

}