package mk.com.store.games.gamestore.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
@Data
@Document(value = "users")
public class User {

    @Id
    private String id;

    @Field
    @Indexed(unique = true)
    private String username;
    
    @Field
    private String password;
    
    @Field
    private String email;

    @DBRef
    private List<Game> games;

    @DBRef
    private List<Publisher> publishers;

    @DBRef
    private Set<Role> roles;

    @DBRef
    private Cart cart;

    @DBRef
    private Wishlist wishlist;

    public User(String username, String password, String email, Cart cart, Wishlist wishlist){
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = new HashSet<>();
        this.cart = cart;
        this.wishlist = wishlist;
        games = new ArrayList<>();
        publishers = new ArrayList<>();
    }
}