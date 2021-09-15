package mk.com.store.games.gamestore.service.developer;

import mk.com.store.games.gamestore.model.*;
import mk.com.store.games.gamestore.model.enumeration.ERole;
import mk.com.store.games.gamestore.model.enumeration.Genre;
import mk.com.store.games.gamestore.model.exception.DeveloperNotFoundException;
import mk.com.store.games.gamestore.model.exception.PublisherNotFoundException;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;
import mk.com.store.games.gamestore.service.DeveloperService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Collectors;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureDataMongo
@ActiveProfiles("test")
public class RemoveDeveloperServiceTest {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private DeveloperService developerService;
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    private User user_3;
    private Developer developer_empty;
    private Developer developer_valid;
    private Developer developer_stray;
    
    @BeforeEach
    public void init() {
        Role role_1 = mongoTemplate.save(new Role(ERole.ROLE_USER));
        Role role_2 = mongoTemplate.save(new Role(ERole.ROLE_PUBLISHER));
        Role role_3 = mongoTemplate.save(new Role(ERole.ROLE_ADMIN));
        Set<Role> roles = new HashSet<>();
        roles.add(role_1);
        Developer developer_1 = mongoTemplate.save(new Developer("Developer_1"));
        Developer developer_2 = mongoTemplate.save(new Developer("Developer_2"));
        Developer developer_3 = mongoTemplate.save(new Developer("Developer_3"));
        Publisher publisher_1 = new Publisher("Publisher_1", "Short description for publisher 1");
        publisher_1.getStudios().add(developer_1);
        publisher_1.getStudios().add(developer_2);
        mongoTemplate.save(publisher_1);
        Publisher publisher_2 = new Publisher("Publisher_2", "Short description for publisher 2");
        publisher_2.getStudios().add(developer_3);
        mongoTemplate.save(publisher_2);
        mongoTemplate.save(new Publisher("Publisher_3", "Short description for publisher 3"));
        Cart cart_1 = mongoTemplate.save(new Cart());
        Cart cart_2 = mongoTemplate.save(new Cart());
        Cart cart_3 = mongoTemplate.save(new Cart());
        Wishlist wishlist_1 = mongoTemplate.save(new Wishlist());
        Wishlist wishlist_2 = mongoTemplate.save(new Wishlist());
        Wishlist wishlist_3 = mongoTemplate.save(new Wishlist());
        User user_1 = new User("user_1", passwordEncoder.encode("user1_password"), "user_1@gmail.com", cart_1, wishlist_1);
        user_1.setRoles(roles);
        user_1 = mongoTemplate.save(user_1);
        User user_2 = new User("user_2", passwordEncoder.encode("user2_password"), "user_2@gmail.com", cart_2, wishlist_2);
        user_2 = mongoTemplate.save(user_2);
        user_2.setRoles(roles);
        user_3 = new User("user_3", passwordEncoder.encode("user3_password"), "user_3@gmail.com", cart_3, wishlist_3);
        roles.add(role_2);
        user_3.setRoles(roles);
        user_3 = mongoTemplate.save(user_3);
        List<Publisher> publishers = mongoTemplate.findAll(Publisher.class);
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
        developer_valid = developersPublisher2.get(0);
        developer_empty = publishers.get(0).getStudios().get(1);
        developer_stray = mongoTemplate.save(new Developer("stray"));
        List<Game> result = new ArrayList<>();
        game_1 = mongoTemplate.save(game_1);
        game_2 = mongoTemplate.save(game_2);
        game_3 = mongoTemplate.save(game_3);
        user_2.getGames().add(game_1);
        user_3.getPublishers().add(publishers.get(0));
        user_3.getGames().add(game_1);
        user_3.getGames().add(game_2);
        mongoTemplate.save(user_2);
        user_3 = mongoTemplate.save(user_3);
    }
    
    
    public static Collection<Object[]> editParameters() {
        return Arrays.asList(new Object[][]{
                {null, IllegalArgumentException.class, "DeveloperId can not be null1"},
                {"", IllegalArgumentException.class, "DeveloperId can not be empty string"},
                {"Hello Developer", IllegalArgumentException.class, "DeveloperId can not contain empty spaces"},
                {"invalid", DeveloperNotFoundException.class, "Could not find a publisher with the specified id"},
                {"valid", PublisherNotFoundException.class, "Could not find a publisher with the specified id"},
                {"empty", null, ""},
                {"valid", null, ""},
        });
    }
    
    @ParameterizedTest
    @MethodSource("editParameters")
    public void editPublisherParamsTest(String developerId, Class<Exception> exceptionClass, String message) throws UserNotFoundException, PublisherNotFoundException, DeveloperNotFoundException {
        if (exceptionClass == null) {
            switch (developerId) {
                case "empty" -> {
                    Assertions.assertTrue(developerService.removeDeveloper(developer_empty.getId()).get());
                }
                case "valid" -> {
                    Assertions.assertTrue(developerService.removeDeveloper(developer_valid.getId()).get());
                }
                default -> {
                    throw new RuntimeException();
                }
            }
        } else {
            if (developerId != null && developerId.equals("valid")) {
                Assertions.assertThrows(exceptionClass, () -> developerService.removeDeveloper(developer_stray.getId()), message);
            } else {
                Assertions.assertThrows(exceptionClass, () -> developerService.removeDeveloper(developerId), message);
            }
        }
    }
    
    @AfterEach
    public void cleanUp() {
        mongoTemplate.dropCollection(Role.class);
        mongoTemplate.dropCollection(Publisher.class);
        mongoTemplate.dropCollection(Developer.class);
        mongoTemplate.dropCollection(Game.class);
        mongoTemplate.dropCollection(User.class);
    }
}
