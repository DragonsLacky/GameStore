package mk.com.store.games.gamestore.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import mk.com.store.games.gamestore.model.Cart;
import mk.com.store.games.gamestore.model.Game;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    List<Cart> findByGames(Game games);
}