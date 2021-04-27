package mk.com.store.games.gamestore.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import mk.com.store.games.gamestore.model.Developer;

@Repository
public interface DeveloperRepository extends MongoRepository<Developer, String> {

}