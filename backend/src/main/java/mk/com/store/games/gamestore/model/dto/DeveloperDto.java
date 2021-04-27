package mk.com.store.games.gamestore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeveloperDto {
    private String name;
    private String publisherId;
    private String username;
}