package mk.com.store.games.gamestore.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mk.com.store.games.gamestore.model.Game;
import mk.com.store.games.gamestore.model.dto.CartDto;
import mk.com.store.games.gamestore.model.exception.GameNotFoundException;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;
import mk.com.store.games.gamestore.service.CartService;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/cart")
public class CartController {
    
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('PUBLISHER') or hasRole('ADMIN')")
    @GetMapping("/{username}")
    public List<Game> getAllGamesInCart(@PathVariable String username) throws UserNotFoundException {
        return cartService.getCartGames(username);
    }

	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PutMapping("/add")
    public List<Game> addGame(@RequestBody CartDto cartDto) throws UserNotFoundException, GameNotFoundException {
        return cartService.addToCart(cartDto);
    }
    
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/remove")
    public List<Game> removeGame(@RequestBody CartDto cartDto) throws UserNotFoundException {
        return cartService.removeFromCart(cartDto);
    }

    @PreAuthorize("hasRole('USER') or hasRole('PUBLISHER') or hasRole('ADMIN')")
    @PostMapping("/buy/{username}")
    public List<Game> buyAll(@PathVariable String username) throws UserNotFoundException {
        return cartService.buy(username);
    }

    @PreAuthorize("hasRole('USER') or hasRole('PUBLISHER') or hasRole('ADMIN')")
    @DeleteMapping("/clear/{username}")
    public List<Game> clear(@PathVariable String username) throws UserNotFoundException {
        return cartService.clear(username);
    }
}