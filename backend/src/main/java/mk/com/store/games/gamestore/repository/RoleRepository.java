package mk.com.store.games.gamestore.repository;

import org.springframework.stereotype.Repository;

import mk.com.store.games.gamestore.model.Role;
import mk.com.store.games.gamestore.model.enumeration.ERole;

import java.util.Optional;


import org.springframework.data.mongodb.repository.MongoRepository;


@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}