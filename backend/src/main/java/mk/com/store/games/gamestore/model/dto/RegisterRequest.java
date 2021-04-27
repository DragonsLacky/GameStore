package mk.com.store.games.gamestore.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegisterRequest {
    String username;
    String email;
    String password;
    List<String> roles;
}