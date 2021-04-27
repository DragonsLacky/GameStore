package mk.com.store.games.gamestore.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Document(value = "wishlists")
public class Wishlist {
    
    @Id
    String id;

    @DBRef
    List<Game> games;

    public Wishlist() {
        games = new ArrayList<>();
    }
}