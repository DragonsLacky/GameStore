package mk.com.store.games.gamestore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartDto {

    private String username;
    private String gameId;
}