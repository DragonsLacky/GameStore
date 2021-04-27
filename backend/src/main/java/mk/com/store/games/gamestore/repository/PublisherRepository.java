package mk.com.store.games.gamestore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import mk.com.store.games.gamestore.model.Developer;
import mk.com.store.games.gamestore.model.Publisher;
import mk.com.store.games.gamestore.model.User;

@Repository
public interface PublisherRepository extends MongoRepository<Publisher, String> {
    Optional<Publisher> findByStudios(Developer studios);
}