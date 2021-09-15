package mk.com.store.games.gamestore.web.controller;

import mk.com.store.games.gamestore.model.exception.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mk.com.store.games.gamestore.model.Game;
import mk.com.store.games.gamestore.model.Role;
import mk.com.store.games.gamestore.model.User;
import mk.com.store.games.gamestore.model.dto.GameDto;
import mk.com.store.games.gamestore.model.dto.UserSearchDto;
import mk.com.store.games.gamestore.model.enumeration.ERole;
import mk.com.store.games.gamestore.repository.RoleRepository;
import mk.com.store.games.gamestore.repository.UserRepository;
import mk.com.store.games.gamestore.service.GameService;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/game")
public class GameController {
    
    private final GameService gameService; 

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }
    
    @PreAuthorize("hasRole('USER') or hasRole('PUBLISHER') or hasRole('ADMIN')")
    @PostMapping("/owned")
    public List<Game> getUserGames(@RequestBody UserSearchDto userSearchDto) throws UserNotFoundException {
        return gameService.getAllGamesByUser(userSearchDto.getUsername());
    }

    @GetMapping("/search/{term}")
    public List<Game> searchGames(@PathVariable String term){
        return gameService.searchByTitle(term);
    }
    
    @GetMapping("/genre/{genre}")
    public List<Game> getGamesByGenre(@PathVariable String genre){
        return gameService.getAllGamesByGenre(genre);
    }

    @PreAuthorize("hasRole('USER') or hasRole('PUBLISHER') or hasRole('ADMIN')")
    @PostMapping("/created")
    public List<Game> getUserCreatedGames(@RequestBody UserSearchDto userSearchDto) throws UserNotFoundException{
        return gameService.getAllGamesByUser(userSearchDto.getUsername());
    }

    @GetMapping("/dev/{devId}")
    public List<Game> getDevGames(@PathVariable String devId) throws UserNotFoundException, DeveloperNotFoundException {
        return gameService.getAllGamesByDev(devId);
    }

    @PreAuthorize("hasRole('PUBLISHER') or hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Game> addGame(@RequestBody GameDto gameDto) throws PublisherNotFoundException, DeveloperNotFoundException, UserNotFoundException, DeveloperNotContainedInPublisherException {
        return gameService.addGame(gameDto)
            .map(game -> ResponseEntity.ok().body(game))
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PreAuthorize("hasRole('PUBLISHER') or hasRole('ADMIN')")
    @PutMapping("/edit/{gameId}")
    public ResponseEntity<Game> editGame(@PathVariable String gameId,@RequestBody GameDto gameDto) throws PublisherNotFoundException, DeveloperNotFoundException, GameNotFoundException, DeveloperNotContainedInPublisherException {
        return gameService.editGame(gameId,gameDto)
            .map(game -> ResponseEntity.ok().body(game))
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PreAuthorize("hasRole('PUBLISHER') or hasRole('ADMIN')")
    @DeleteMapping("/remove/{id}")
    public ResponseEntity deleteGame(@PathVariable String id) throws GameNotFoundException {
        this.gameService.removeGame(id);
        return ResponseEntity.ok().build();
    }
}