package mk.com.store.games.gamestore.service;

import java.util.List;

import mk.com.store.games.gamestore.model.Game;
import mk.com.store.games.gamestore.model.dto.WishlistDto;
import mk.com.store.games.gamestore.model.exception.GameNotFoundException;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;

public interface WishlistService {

    public List<Game> getWishLListGames(String username) throws UserNotFoundException;

    public List<Game> addToWishlist(WishlistDto wishlistDto) throws UserNotFoundException, GameNotFoundException;

    public List<Game> removeFromWishlist(WishlistDto wishlistDto) throws UserNotFoundException;

    public List<Game> clear(String username) throws UserNotFoundException;
}