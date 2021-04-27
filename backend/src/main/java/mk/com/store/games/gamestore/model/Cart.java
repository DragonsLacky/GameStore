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
@Document(value = "carts")
public class Cart {
    
    @Id
    private String id;
    
    @DBRef
    private List<Game> games;

    public Cart() {
        games = new ArrayList<>();
    }
}