package mk.com.store.games.gamestore.service.game;

import mk.com.store.games.gamestore.model.*;
import mk.com.store.games.gamestore.model.enumeration.ERole;
import mk.com.store.games.gamestore.model.enumeration.Genre;
import mk.com.store.games.gamestore.model.exception.GameNotFoundException;
import mk.com.store.games.gamestore.repository.*;
import mk.com.store.games.gamestore.service.GameService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class RemoveGameServiceTest {
    @Autowired
    private GameService gameService;
    
    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private WishlistRepository wishlistRepository;
    
    @Autowired
    private PublisherRepository publisherRepository;
    
    @Autowired
    private DeveloperRepository developerRepository;
    
    private User user_1;
    private User user_2;
    private User user_3;
    
    @BeforeEach
    public void init() {
        Role role_1 = roleRepository.insert(new Role(ERole.ROLE_USER));
        Role role_2 = roleRepository.insert(new Role(ERole.ROLE_PUBLISHER));
        Role role_3 = roleRepository.insert(new Role(ERole.ROLE_ADMIN));
        Set<Role> roles = new HashSet<>();
        roles.add(role_1);
        Developer developer_1 = developerRepository.insert(new Developer("Developer_1"));
        Developer developer_2 = developerRepository.insert(new Developer("Developer_2"));
        Developer developer_3 = developerRepository.insert(new Developer("Developer_3"));
        Publisher publisher_1 = new Publisher("Publisher_1", "Short description for publisher 1");
        publisher_1.getStudios().add(developer_1);
        publisher_1.getStudios().add(developer_2);
        publisherRepository.insert(publisher_1);
        Publisher publisher_2 = new Publisher("Publisher_2", "Short description for publisher 2");
        publisher_2.getStudios().add(developer_3);
        publisherRepository.insert(publisher_2);
        publisherRepository.insert(new Publisher("Publisher_3", "Short description for publisher 3"));
        Cart cart_1 = cartRepository.insert(new Cart());
        Cart cart_2 = cartRepository.insert(new Cart());
        Cart cart_3 = cartRepository.insert(new Cart());
        Wishlist wishlist_1 = wishlistRepository.insert(new Wishlist());
        Wishlist wishlist_2 = wishlistRepository.insert(new Wishlist());
        Wishlist wishlist_3 = wishlistRepository.insert(new Wishlist());
        user_1 = new User("user_1", passwordEncoder.encode("user1_password"), "user_1@gmail.com", cart_1, wishlist_1);
        user_1.setRoles(roles);
        user_1 = userRepository.insert(user_1);
        user_2 = new User("user_2", passwordEncoder.encode("user2_password"), "user_2@gmail.com", cart_2, wishlist_2);
        user_2 = userRepository.insert(user_2);
        user_2.setRoles(roles);
        user_3 = new User("user_3", passwordEncoder.encode("user3_password"), "user_3@gmail.com", cart_3, wishlist_3);
        roles.add(role_2);
        user_3.setRoles(roles);
        user_3 = userRepository.insert(user_3);
    }
    
    @Test
    public void RemoveGameNullIdTest(){
        List<Publisher> publishers = publisherRepository.findAll();
        List<Developer> developersPublisher1 = publishers.get(0).getStudios();
        List<Developer> developersPublisher2 = publishers.get(1).getStudios();
        Game game_1 =
                new Game("title_1",
                        "description for title_1",
                        10.2,
                        developersPublisher1.get(0),
                        publishers.get(0),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_2 =
                new Game("title_2",
                        "description for title_1",
                        10.2,
                        developersPublisher1.get(0),
                        publishers.get(0),
                        Arrays.stream((new Genre[]{Genre.RACING, Genre.ACTION})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.RACING, Genre.RPG})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        game_1 = gameRepository.insert(game_1);
        game_2 = gameRepository.insert(game_2);
        game_3 = gameRepository.insert(game_3);
        user_2.getGames().add(game_1);
        user_3.getPublishers().add(publishers.get(0));
        user_3.getGames().add(game_1);
        user_3.getGames().add(game_2);
        userRepository.save(user_1);
        userRepository.save(user_2);
    
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> gameService.removeGame(null));
        Assertions.assertEquals("Id cannot be null!",exception.getMessage());
    }
    
    @Test
    public void RemoveGameEmptyIdTest(){
        List<Publisher> publishers = publisherRepository.findAll();
        List<Developer> developersPublisher1 = publishers.get(0).getStudios();
        List<Developer> developersPublisher2 = publishers.get(1).getStudios();
        Game game_1 =
                new Game("title_1",
                        "description for title_1",
                        10.2,
                        developersPublisher1.get(0),
                        publishers.get(0),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_2 =
                new Game("title_2",
                        "description for title_1",
                        10.2,
                        developersPublisher1.get(0),
                        publishers.get(0),
                        Arrays.stream((new Genre[]{Genre.RACING, Genre.ACTION})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.RACING, Genre.RPG})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        game_1 = gameRepository.insert(game_1);
        game_2 = gameRepository.insert(game_2);
        game_3 = gameRepository.insert(game_3);
        user_2.getGames().add(game_1);
        user_3.getPublishers().add(publishers.get(0));
        user_3.getGames().add(game_1);
        user_3.getGames().add(game_2);
        userRepository.save(user_1);
        userRepository.save(user_2);
        
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> gameService.removeGame(""));
        Assertions.assertEquals("Id cannot be empty string!",exception.getMessage());
    }
    
    @Test
    public void RemoveGameInvalidIdTest(){
        List<Publisher> publishers = publisherRepository.findAll();
        List<Developer> developersPublisher1 = publishers.get(0).getStudios();
        List<Developer> developersPublisher2 = publishers.get(1).getStudios();
        Game game_1 =
                new Game("title_1",
                        "description for title_1",
                        10.2,
                        developersPublisher1.get(0),
                        publishers.get(0),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_2 =
                new Game("title_2",
                        "description for title_1",
                        10.2,
                        developersPublisher1.get(0),
                        publishers.get(0),
                        Arrays.stream((new Genre[]{Genre.RACING, Genre.ACTION})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.RACING, Genre.RPG})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        game_1 = gameRepository.insert(game_1);
        game_2 = gameRepository.insert(game_2);
        game_3 = gameRepository.insert(game_3);
        user_2.getGames().add(game_1);
        user_3.getPublishers().add(publishers.get(0));
        user_3.getGames().add(game_1);
        user_3.getGames().add(game_2);
        userRepository.save(user_1);
        userRepository.save(user_2);
        
        Exception exception = Assertions.assertThrows(GameNotFoundException.class, () -> gameService.removeGame("invalid"));
        Assertions.assertEquals("Could not find the specified game",exception.getMessage());
    }
    
    @Test
    public void RemoveGameValidParametersTest() throws GameNotFoundException {
        List<Publisher> publishers = publisherRepository.findAll();
        List<Developer> developersPublisher1 = publishers.get(0).getStudios();
        List<Developer> developersPublisher2 = publishers.get(1).getStudios();
        Game game_1 =
                new Game("title_1",
                        "description for title_1",
                        10.2,
                        developersPublisher1.get(0),
                        publishers.get(0),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_2 =
                new Game("title_2",
                        "description for title_1",
                        10.2,
                        developersPublisher1.get(0),
                        publishers.get(0),
                        Arrays.stream((new Genre[]{Genre.RACING, Genre.ACTION})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.RACING, Genre.RPG})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        game_1 = gameRepository.insert(game_1);
        game_2 = gameRepository.insert(game_2);
        game_3 = gameRepository.insert(game_3);
        user_2.getGames().add(game_1);
        user_3.getPublishers().add(publishers.get(0));
        user_3.getGames().add(game_1);
        user_3.getGames().add(game_2);
        userRepository.save(user_1);
        userRepository.save(user_2);
        
        Assertions.assertTrue(gameService.removeGame(game_1.getId()).get());
        Assertions.assertFalse(userRepository.findByUsername(user_2.getUsername()).get().getGames().contains(game_1));
        Assertions.assertFalse(userRepository.findByUsername(user_3.getUsername()).get().getGames().contains(game_1));
    }
    
    
    @AfterEach
    public void cleanUp() {
        roleRepository.deleteAll();
        publisherRepository.deleteAll();
        developerRepository.deleteAll();
        gameRepository.deleteAll();
        userRepository.deleteAll();
    }
}
