package mk.com.store.games.gamestore.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mk.com.store.games.gamestore.model.enumeration.Genre;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor
@Getter
@Data
@Document(collection = "games")
public class Game {
    
    @Id
    String id;
    
    @Field
    String title;
    
    @Field
    String description;
    
    @Field
    Double price;

    @DBRef(lazy = true)
    Developer developer;
    
    @DBRef(lazy = true)
    Publisher publisher;

    @Field
    List<Genre> genres;

    public Game(String title, String description, Double price, Developer developer, Publisher publisher, List<Genre> genres) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.developer = developer;
        this.publisher = publisher;
        this.genres = genres;
    }
    
}
