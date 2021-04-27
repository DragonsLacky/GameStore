package mk.com.store.games.gamestore.service.inplementation;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mk.com.store.games.gamestore.model.Cart;
import mk.com.store.games.gamestore.model.User;
import mk.com.store.games.gamestore.model.Wishlist;
import mk.com.store.games.gamestore.model.Role;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;
import mk.com.store.games.gamestore.repository.CartRepository;
import mk.com.store.games.gamestore.repository.UserRepository;
import mk.com.store.games.gamestore.repository.WishlistRepository;
import mk.com.store.games.gamestore.service.UserService;

@Service
public class UserServiceImplementation implements UserService {
    
    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImplementation(UserRepository userRepository, WishlistRepository wishlistRepository,
            CartRepository cartRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.wishlistRepository = wishlistRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<User> register(String username, String password, String email, Set<Role> roles) {
        Wishlist wishlist = wishlistRepository.save(new Wishlist());
        Cart cart = cartRepository.save(new Cart());
        User user = new User(username, passwordEncoder.encode(password), email, cart, wishlist); 
        user.setRoles(roles);
        userRepository.save(user);
        return Optional.of(user);
    }


    @Override
    public Optional<User> getUser(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username).stream().findFirst().orElseThrow(UserNotFoundException::new);
        return Optional.of(user);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).map((item) -> true).orElseGet(() -> false);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).map((item) -> true).orElseGet(() -> false);
    }

    @Override
    public Optional<User> removeUser(String userId) {
        // TODO Auto-generated method stub
        return null;
    }

    
}