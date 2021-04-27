package mk.com.store.games.gamestore.service;

import java.util.List;

import mk.com.store.games.gamestore.model.Game;
import mk.com.store.games.gamestore.model.dto.CartDto;
import mk.com.store.games.gamestore.model.exception.GameNotFoundException;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;


public interface CartService { 
    
    public List<Game> getCartGames(String username) throws UserNotFoundException;

    public List<Game> addToCart(CartDto cartDto) throws UserNotFoundException, GameNotFoundException;

    public List<Game> removeFromCart(CartDto cartDto) throws UserNotFoundException;

    public List<Game> buy(String username) throws UserNotFoundException;

    public List<Game> clear(String username) throws UserNotFoundException;
}