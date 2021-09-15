package mk.com.store.games.gamestore.api.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import mk.com.store.games.gamestore.model.*;
import mk.com.store.games.gamestore.model.dto.CartDto;
import mk.com.store.games.gamestore.model.dto.GameDto;
import mk.com.store.games.gamestore.model.dto.LoginRequest;
import mk.com.store.games.gamestore.model.dto.UserSearchDto;
import mk.com.store.games.gamestore.model.enumeration.ERole;
import mk.com.store.games.gamestore.model.enumeration.Genre;
import mk.com.store.games.gamestore.service.CartService;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureDataMongo
@ActiveProfiles("test")
public class GameControllerTest {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private MockMvc mvc;
    
    private Game game_2;
    
    private final List<String> tokens = new ArrayList<>();
    
    @BeforeEach
    public void init() throws Exception {
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
        User user_3 = new User("user_3", passwordEncoder.encode("user3_password"), "user_3@gmail.com", cart_3, wishlist_3);
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
        game_2 =
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
        game_1 = mongoTemplate.save(game_1);
        game_2 = mongoTemplate.save(game_2);
        game_3 = mongoTemplate.save(game_3);
        user_2.getGames().add(game_1);
        cart_2.getGames().add(game_2);
        user_3.getPublishers().add(publishers.get(0));
        user_3.getGames().add(game_1);
        user_3.getGames().add(game_2);
        mongoTemplate.save(user_2);
        mongoTemplate.save(cart_2);
        mongoTemplate.save(user_3);
        
        MvcResult mvcResult = this.mvc.perform(post("/api/auth/login")
                        .content(asJsonString(new LoginRequest("user_1", "user1_password")))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        tokens.add(jsonObject.getString("token"));
        mvcResult = this.mvc.perform(post("/api/auth/login")
                        .content(asJsonString(new LoginRequest("user_2", "user2_password")))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        tokens.add(jsonObject.getString("token"));
        mvcResult = this.mvc.perform(post("/api/auth/login")
                        .content(asJsonString(new LoginRequest("user_3", "user3_password")))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        tokens.add(jsonObject.getString("token"));
    }
    
    public static Collection<Object[]> userParameters() {
        return Arrays.asList(new Object[][]{
                {"user_3", 2},
                {"user_2", 1},
                {"user_1", 0},
        });
    }
    
    @Test
    public void getAllGamesTest() throws Exception {
        MvcResult mvcResult = this.mvc.perform(get("/api/game/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        String expected = asJsonString(mongoTemplate.findAll(Game.class));
        Assertions.assertEquals(expected, result);
    }
    
    @ParameterizedTest
    @MethodSource("userParameters")
    public void getAllOwnedGamesTest(String username, Integer tokenIndex) throws Exception {
        MvcResult mvcResult = this.mvc.perform(post("/api/game/owned")
                        .content(asJsonString(new UserSearchDto(username, "")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.get(tokenIndex))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        String expected = asJsonString(mongoTemplate.findOne(new Query(new Criteria("username").is(username)), User.class).getGames());
        Assertions.assertEquals(expected, result);
    }
    
    public static Collection<Object[]> termParameters() {
        return Arrays.asList(new Object[][]{
                {"title_1"},
                {"_2"},
                {"difFERent_one"},
        });
    }
    
    @ParameterizedTest
    @MethodSource("userParameters")
    public void searchGameByTermTest(String term) throws Exception {
        MvcResult mvcResult = this.mvc.perform(get("/api/game/search/" + term)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        String expected = asJsonString(mongoTemplate.findAll(Game.class).stream().filter(game -> game.getTitle().toLowerCase().contains(term.toLowerCase())).collect(Collectors.toList()));
        Assertions.assertEquals(expected, result);
    }
    
    @Test
    public void publishNewGameTest() throws Exception {
        User user = mongoTemplate.findOne(new Query(new Criteria("username").is("user_3")), User.class);
        MvcResult mvcResult = this.mvc.perform(post("/api/game/add")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.get(2))
                        .content(asJsonString(new GameDto(
                                                user.getUsername(),
                                                "Some other Game",
                                                "a good description",
                                                59.99,
                                                user.getPublishers().get(0).getStudios().get(0).getId(),
                                                user.getPublishers().get(0).getId(),
                                                Arrays.stream((new Genre[]{Genre.RACING, Genre.RPG})).map(Enum::toString).collect(Collectors.toList())
                                        )
                                )
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        String expected = asJsonString(mongoTemplate.findOne(new Query(new Criteria("title").is("Some other Game")), Game.class));
        Assertions.assertEquals(expected, result);
    }
    
    @Test
    public void publishNewGameUnahtorizedTest() throws Exception {
        User user = mongoTemplate.findOne(new Query(new Criteria("username").is("user_3")), User.class);
        this.mvc.perform(post("/api/game/add")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.get(0))
                        .content(asJsonString(new GameDto(
                                                user.getUsername(),
                                                "Some other Game",
                                                "a good description",
                                                59.99,
                                                user.getPublishers().get(0).getStudios().get(0).getId(),
                                                user.getPublishers().get(0).getId(),
                                                Arrays.stream((new Genre[]{Genre.RACING, Genre.RPG})).map(Enum::toString).collect(Collectors.toList())
                                        )
                                )
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
    
    @Test
    public void editExistingGameTest() throws Exception {
        User user = mongoTemplate.findOne(new Query(new Criteria("username").is("user_3")), User.class);
        MvcResult mvcResult = this.mvc.perform(put("/api/game/edit/" + game_2.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.get(2))
                        .content(asJsonString(new GameDto(
                                                user.getUsername(),
                                                "Change Title",
                                                "another Description",
                                                59.99,
                                                user.getPublishers().get(0).getStudios().get(0).getId(),
                                                user.getPublishers().get(0).getId(),
                                                Arrays.stream((new Genre[]{Genre.ADVENTURE, Genre.ACTION})).map(Enum::toString).collect(Collectors.toList())
                                        )
                                )
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        String expected = asJsonString(game_2);
        Assertions.assertNotEquals(expected, result);
        expected = asJsonString(mongoTemplate.findById(  game_2.getId(),Game.class));
        Assertions.assertEquals(expected, result);
    }
    @Test
    public void editExistingGameUnauthorizedTest() throws Exception {
        User user = mongoTemplate.findOne(new Query(new Criteria("username").is("user_3")), User.class);
        this.mvc.perform(put("/api/game/edit/" + game_2.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.get(0))
                        .content(asJsonString(new GameDto(
                                                user.getUsername(),
                                                "Change Title",
                                                "another Description",
                                                59.99,
                                                user.getPublishers().get(0).getStudios().get(0).getId(),
                                                user.getPublishers().get(0).getId(),
                                                Arrays.stream((new Genre[]{Genre.ADVENTURE, Genre.ACTION})).map(Enum::toString).collect(Collectors.toList())
                                        )
                                )
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
    
    @Test
    public void removeExistingGameTest() throws Exception {
        this.mvc.perform(delete("/api/game/remove/" + game_2.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.get(2))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Assertions.assertNull(mongoTemplate.findById(game_2.getId(),Game.class));
    }
    
    @Test
    public void removeExistingGameUnAuthorizedTest() throws Exception {
        this.mvc.perform(delete("/api/game/remove/" + game_2.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.get(0))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
    
    
    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
