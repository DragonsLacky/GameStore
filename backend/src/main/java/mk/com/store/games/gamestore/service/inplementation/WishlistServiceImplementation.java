package mk.com.store.games.gamestore.service.inplementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import mk.com.store.games.gamestore.model.Cart;
import mk.com.store.games.gamestore.model.Game;
import mk.com.store.games.gamestore.model.User;
import mk.com.store.games.gamestore.model.Wishlist;
import mk.com.store.games.gamestore.model.dto.WishlistDto;
import mk.com.store.games.gamestore.model.exception.GameNotFoundException;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;
import mk.com.store.games.gamestore.repository.CartRepository;
import mk.com.store.games.gamestore.repository.GameRepository;
import mk.com.store.games.gamestore.repository.UserRepository;
import mk.com.store.games.gamestore.repository.WishlistRepository;
import mk.com.store.games.gamestore.service.WishlistService;

@Service
public class WishlistServiceImplementation implements WishlistService{

    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;
    private final GameRepository gameRepository;

    public WishlistServiceImplementation(UserRepository userRepository, WishlistRepository wishlistRepository,
            GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.wishlistRepository = wishlistRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public List<Game> getWishLListGames(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username).stream().findFirst().orElseThrow(UserNotFoundException::new);
        return user.getWishlist().getGames();
    }

    @Override
    public List<Game> addToWishlist(WishlistDto wishlistDto) throws UserNotFoundException, GameNotFoundException {
        User user = userRepository.findByUsername(wishlistDto.getUsername()).stream().findFirst().orElseThrow(UserNotFoundException::new);
        Game game = gameRepository.findById(wishlistDto.getGameId()).orElseThrow(GameNotFoundException::new);
        Wishlist wishlist = user.getWishlist();
        wishlist.getGames().add(game);
        wishlistRepository.save(wishlist);
        return wishlist.getGames();
    }

    @Override
    public List<Game> removeFromWishlist(WishlistDto wishlistDto) throws UserNotFoundException {
        User user = userRepository.findByUsername(wishlistDto.getUsername()).stream().findFirst().orElseThrow(UserNotFoundException::new);
        Wishlist wishlist = user.getWishlist();
        wishlist.getGames().removeIf(game -> game.getId().equals(wishlistDto.getGameId()));
        wishlistRepository.save(wishlist);
        return wishlist.getGames();
    }

    @Override
    public List<Game> clear(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username).stream().findFirst().orElseThrow(UserNotFoundException::new);
        Wishlist wishlist = user.getWishlist();
        wishlist.setGames(new ArrayList<>());
        wishlistRepository.save(wishlist);
        return wishlist.getGames();
    }

    
}