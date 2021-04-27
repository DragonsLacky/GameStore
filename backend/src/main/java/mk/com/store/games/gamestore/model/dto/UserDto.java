package mk.com.store.games.gamestore.model.dto;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import mk.com.store.games.gamestore.model.Cart;
import mk.com.store.games.gamestore.model.Game;
import mk.com.store.games.gamestore.model.Publisher;
import mk.com.store.games.gamestore.model.Role;
import mk.com.store.games.gamestore.model.Wishlist;

@Data
@AllArgsConstructor
public class UserDto {
    private String username;    
    private String email;
    private List<Game> games;
    private List<Publisher> publishers;
    private Set<Role> roles;
    private Cart cart;
    private Wishlist wishlist;
}