package mk.com.store.games.gamestore.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import mk.com.store.games.gamestore.model.Game;
import mk.com.store.games.gamestore.model.Wishlist;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, String> {
    List<Wishlist> findByGames(Game games);
}