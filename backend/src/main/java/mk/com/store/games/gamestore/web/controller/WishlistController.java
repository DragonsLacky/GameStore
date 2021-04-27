package mk.com.store.games.gamestore.web.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mk.com.store.games.gamestore.model.Game;
import mk.com.store.games.gamestore.model.dto.UserSearchDto;
import mk.com.store.games.gamestore.model.dto.WishlistDto;
import mk.com.store.games.gamestore.model.exception.GameNotFoundException;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;
import mk.com.store.games.gamestore.service.WishlistService;

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
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }
    
    @PreAuthorize("hasRole('USER') or hasRole('PUBLISHER') or hasRole('ADMIN')")
    @GetMapping("/{username}")
    public List<Game> getAllGamesFromWishlist(@PathVariable String username) throws UserNotFoundException {
        return wishlistService.getWishLListGames(username);
    }

    @PreAuthorize("hasRole('USER') or hasRole('PUBLISHER') or hasRole('ADMIN')")
    @PutMapping("/add")
    public List<Game> addGame(@RequestBody WishlistDto wishlistDto) throws UserNotFoundException, GameNotFoundException {
        return wishlistService.addToWishlist(wishlistDto);
    }
    
    @PreAuthorize("hasRole('USER') or hasRole('PUBLISHER') or hasRole('ADMIN')")
    @PostMapping("/remove")
    public List<Game> removeGame(@RequestBody WishlistDto wishlistDto) throws UserNotFoundException {
        return wishlistService.removeFromWishlist(wishlistDto);
    }

    @PreAuthorize("hasRole('USER') or hasRole('PUBLISHER') or hasRole('ADMIN')")
    @DeleteMapping("/{username}")
    public List<Game> clear(@PathVariable String username) throws UserNotFoundException {
        return wishlistService.clear(username);
    }
    
}