package mk.com.store.games.gamestore.service.inplementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import mk.com.store.games.gamestore.model.Cart;
import mk.com.store.games.gamestore.model.Game;
import mk.com.store.games.gamestore.model.User;
import mk.com.store.games.gamestore.model.Wishlist;
import mk.com.store.games.gamestore.model.dto.CartDto;
import mk.com.store.games.gamestore.model.exception.GameNotFoundException;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;
import mk.com.store.games.gamestore.repository.CartRepository;
import mk.com.store.games.gamestore.repository.GameRepository;
import mk.com.store.games.gamestore.repository.UserRepository;
import mk.com.store.games.gamestore.repository.WishlistRepository;
import mk.com.store.games.gamestore.service.CartService;

@Service
public class CartServiceImplementation implements CartService {
    
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final WishlistRepository wishlistRepository;
    
    public CartServiceImplementation(CartRepository cartRepository, UserRepository userRepository,
                                     GameRepository gameRepository, WishlistRepository wishlistRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.wishlistRepository = wishlistRepository;
    }
    
    @Override
    public List<Game> getCartGames(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username).stream().findFirst().orElseThrow(UserNotFoundException::new);
        return user.getCart().getGames();
    }
    
    @Override
    public List<Game> addToCart(CartDto cartDto) throws UserNotFoundException, GameNotFoundException {
        User user = userRepository.findByUsername(cartDto.getUsername()).stream().findFirst().orElseThrow(UserNotFoundException::new);
        Game game = gameRepository.findById(cartDto.getGameId()).orElseThrow(GameNotFoundException::new);
        Cart cart = user.getCart();
        if (user.getGames().stream().anyMatch(g -> g.getId().equals(game.getId()))) {
            return cart.getGames();
        }
        if (cart.getGames().stream().anyMatch(g -> g.getId().equals(game.getId()))){
            return cart.getGames();
        }
        cart.getGames().add(game);
        cartRepository.save(cart);
        return cart.getGames();
    }
    
    @Override
    public List<Game> removeFromCart(CartDto cartDto) throws UserNotFoundException {
        User user = userRepository.findByUsername(cartDto.getUsername()).stream().findFirst().orElseThrow(UserNotFoundException::new);
        Cart cart = user.getCart();
        cart.getGames().removeIf(game -> game.getId().equals(cartDto.getGameId()));
        cartRepository.save(cart);
        return cart.getGames();
    }
    
    @Override
    public List<Game> buy(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username).stream().findFirst().orElseThrow(UserNotFoundException::new);
        Cart cart = user.getCart();
        Wishlist wishlist = user.getWishlist();
        cart.getGames().stream().forEach(game -> user.getGames().add(game));
        cart.getGames().stream().forEach(game -> wishlist.getGames().remove(game));
        cart.setGames(new ArrayList<>());
        userRepository.save(user);
        wishlistRepository.save(wishlist);
        cartRepository.save(cart);
        return cart.getGames();
    }
    
    @Override
    public List<Game> clear(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username).stream().findFirst().orElseThrow(UserNotFoundException::new);
        Cart cart = user.getCart();
        cart.setGames(new ArrayList<>());
        cartRepository.save(cart);
        return cart.getGames();
    }
    
}