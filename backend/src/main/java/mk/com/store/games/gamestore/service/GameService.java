package mk.com.store.games.gamestore.service;

import java.util.List;
import java.util.Optional;

import mk.com.store.games.gamestore.model.Game;
import mk.com.store.games.gamestore.model.dto.GameDto;
import mk.com.store.games.gamestore.model.exception.DeveloperNotFoundException;
import mk.com.store.games.gamestore.model.exception.GameNotFoundException;
import mk.com.store.games.gamestore.model.exception.PublisherNotFoundException;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;


public interface GameService {
    
    public List<Game> getAllGames();
    
    public Optional<Game> addGame(GameDto gameDto) throws PublisherNotFoundException, DeveloperNotFoundException;

    public List<Game> getAllGamesByUser(String username) throws UserNotFoundException;

    public List<Game> getAllCreatedGamesByUser(String username) throws UserNotFoundException;

    public List<Game> getAllGamesByGenre(String genre);

    public List<Game> getAllGamesByDev(String devId) throws DeveloperNotFoundException;

    public Optional<Boolean> removeGame(String id) throws GameNotFoundException;
}
