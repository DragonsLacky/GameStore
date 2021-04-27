package mk.com.store.games.gamestore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PublisherDto {
    String name;
    String description;
    String username;
}