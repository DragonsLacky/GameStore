package mk.com.store.games.gamestore.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import mk.com.store.games.gamestore.model.Developer;
import mk.com.store.games.gamestore.model.Game;
import mk.com.store.games.gamestore.model.enumeration.Genre;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {
    List<Game> findByDeveloper(Developer developer);
    List<Game> findByGenres(Genre genres);
    List<Game> findAllByTitleContainingIgnoreCase(String title);
}
