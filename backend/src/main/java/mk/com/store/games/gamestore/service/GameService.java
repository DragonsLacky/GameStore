package mk.com.store.games.gamestore.service;

import java.util.List;
import java.util.Optional;

import mk.com.store.games.gamestore.model.Game;
import mk.com.store.games.gamestore.model.dto.GameDto;
import mk.com.store.games.gamestore.model.exception.*;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;


public interface GameService {
    
    public List<Game> getAllGames();
    
    public List<Game> searchByTitle(String term);
    
    public Optional<Game> addGame(GameDto gameDto) throws PublisherNotFoundException, DeveloperNotFoundException, DeveloperNotContainedInPublisherException, UserNotFoundException;

    public List<Game> getAllGamesByUser(String username) throws UserNotFoundException;

    public List<Game> getAllCreatedGamesByUser(String username) throws UserNotFoundException, PublisherNotFoundException;

    public List<Game> getAllGamesByGenre(String genre);

    public List<Game> getAllGamesByDev(String devId) throws DeveloperNotFoundException;

    public Optional<Boolean> removeGame(String id) throws GameNotFoundException;

    public Optional<Game> editGame(String gameId, GameDto gameDto) throws GameNotFoundException, DeveloperNotFoundException, PublisherNotFoundException, DeveloperNotContainedInPublisherException;
}
