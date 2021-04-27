package mk.com.store.games.gamestore.service;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import mk.com.store.games.gamestore.model.User;
import mk.com.store.games.gamestore.model.Role;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;

public interface UserService {

    public Optional<User> getUser(String username) throws UserNotFoundException;

    public List<User> getAllUsers();
    
    public Optional<User> register(String username, String password, String email, Set<Role> roles);

    public Optional<User> removeUser(String userId);

    public Boolean existsByUsername(String username);

    public Boolean existsByEmail(String email);
}