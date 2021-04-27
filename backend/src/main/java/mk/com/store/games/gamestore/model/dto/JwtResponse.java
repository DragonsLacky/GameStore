package mk.com.store.games.gamestore.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    String token;
    String type;
    String username;
    String email;
    List<String> roles;
}