package mk.com.store.games.gamestore.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameDto {
    private String username;
    private String title;
    private String description;
    private Double price;
    private String developerId;
    private String publisherId;
    private List<String> genres;
}