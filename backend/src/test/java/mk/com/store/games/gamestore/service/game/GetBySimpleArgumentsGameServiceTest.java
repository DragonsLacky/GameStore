package mk.com.store.games.gamestore.service.game;

import mk.com.store.games.gamestore.model.*;
import mk.com.store.games.gamestore.model.enumeration.ERole;
import mk.com.store.games.gamestore.model.enumeration.Genre;
import mk.com.store.games.gamestore.model.exception.DeveloperNotFoundException;
import mk.com.store.games.gamestore.service.GameService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureDataMongo
@ActiveProfiles("test")
public class GetBySimpleArgumentsGameServiceTest {
    
    @Autowired
    private GameService gameService;
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @BeforeEach
    public void init() {
        mongoTemplate.save(new Role(ERole.ROLE_USER));
        mongoTemplate.save(new Role(ERole.ROLE_PUBLISHER));
        mongoTemplate.save(new Role(ERole.ROLE_ADMIN));
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
    }
    
    //There are no values in database
    @Test
    public void GetAllGamesEmptyTest() {
        Assertions.assertEquals(this.gameService.getAllGames(), new ArrayList<Game>());
    }
    
    //There is only 1 value in database
    @Test
    public void getAllGamesOneElementTest() {
        List<Publisher> publishers = mongoTemplate.findAll(Publisher.class);
        List<Developer> developers = publishers.get(0).getStudios();
        Game game_1 =
                new Game("title_1",
                        "description for title_1",
                        10.2,
                        developers.get(0),
                        publishers.get(0),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        result.add(game_1);
        Assertions.assertEquals(this.gameService.getAllGames(), result);
    }
    
    //There are multiple values in the database
    @Test
    public void getAllGamesMoreThanOneElementTest() {
        List<Publisher> publishers = mongoTemplate.findAll(Publisher.class);
        List<Developer> developers = publishers.get(0).getStudios();
        Game game_1 =
                new Game("title_1",
                        "description for title_1",
                        10.2,
                        developers.get(0),
                        publishers.get(0),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_2 =
                new Game("title_2",
                        "description for title_1",
                        10.2,
                        developers.get(1),
                        publishers.get(0),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        result.add(game_1);
        result.add(game_2);
        Assertions.assertEquals(this.gameService.getAllGames(), result);
    }
    
    //Term is null
    @Test
    public void getAllGamesBySearchTermNullTest() {
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
                        developersPublisher1.get(1),
                        publishers.get(0),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> this.gameService.searchByTitle(null));
        Assertions.assertEquals(exception.getMessage(), "Search term is null");
    }
    
    //Term is empty string
    @Test
    public void getAllGamesBySearchTermEmptyTest() {
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
                        developersPublisher1.get(1),
                        publishers.get(0),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        result.add(game_1);
        result.add(game_2);
        result.add(game_3);
        Assertions.assertEquals(this.gameService.searchByTitle(""), result);
    }
    
    @Test
    public void getAllGamesBySearchTermNotMatchingTest() {
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
                        developersPublisher1.get(1),
                        publishers.get(0),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        Assertions.assertEquals(this.gameService.searchByTitle("NonMatched"), result);
    }
    
    @Test
    public void getAllGamesBySearchTermMatchesOnlyOnePartiallyTest() {
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
                        developersPublisher1.get(1),
                        publishers.get(0),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        result.add(game_3);
        Assertions.assertEquals(this.gameService.searchByTitle("ent_"), result);
    }
    
    @Test
    public void getAllGamesBySearchTermMatchesOnlyOneFullyTest() {
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
                        developersPublisher1.get(1),
                        publishers.get(0),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        result.add(game_3);
        Assertions.assertEquals(this.gameService.searchByTitle("different_one"), result);
    }
    
    @Test
    public void getAllGamesBySearchTermMatchesMultiplePartiallyTest() {
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
                        developersPublisher1.get(1),
                        publishers.get(0),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        result.add(game_1);
        result.add(game_2);
        Assertions.assertEquals(this.gameService.searchByTitle("tle_"), result);
    }
    
    @Test
    public void getAllGamesBySearchTermMatchesMultipleFullyTest() {
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
                new Game("title_1",
                        "description for title_1",
                        10.2,
                        developersPublisher1.get(1),
                        publishers.get(0),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        result.add(game_1);
        result.add(game_2);
        Assertions.assertEquals(this.gameService.searchByTitle("title_1"), result);
    }
    
    @Test
    public void getAllGamesByDeveloperIdNullTest() {
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
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> gameService.getAllGamesByDev(null));
        Assertions.assertEquals(exception.getMessage(), "Id cannot be null!");
    }
    
    @Test
    public void getAllGamesByDeveloperIdEmptyTest() {
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
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        Exception exception = Assertions.assertThrows(DeveloperNotFoundException.class, () -> gameService.getAllGamesByDev(""));
        Assertions.assertEquals(exception.getMessage(), "Could not find developer with the specified id");
    }
    
    @Test
    public void getAllGamesByDeveloperIdInvalidTest() {
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
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        Exception exception = Assertions.assertThrows(DeveloperNotFoundException.class, () -> gameService.getAllGamesByDev("invalid"));
        Assertions.assertEquals(exception.getMessage(), "Could not find developer with the specified id");
    }
    
    @Test
    public void getAllGamesByDeveloperIdValidNoGamesTest() throws DeveloperNotFoundException {
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
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        Assertions.assertEquals( result , gameService.getAllGamesByDev(developersPublisher1.get(1).getId()));
    }
    
    @Test
    public void getAllGamesByDeveloperIdValidOneGameTest() throws DeveloperNotFoundException {
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
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        result.add(game_3);
        Assertions.assertEquals( result , gameService.getAllGamesByDev(developersPublisher2.get(0).getId()));
    }
    
    @Test
    public void getAllGamesByDeveloperIdValidMultipleGamesTest() throws DeveloperNotFoundException {
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
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        result.add(game_1);
        result.add(game_2);
        Assertions.assertEquals( result , gameService.getAllGamesByDev(developersPublisher1.get(0).getId()));
    }
    
    @Test
    public void getAllGamesByGenreNullTest(){
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
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> gameService.getAllGamesByGenre(null));
        Assertions.assertEquals(exception.getMessage(), "Genre cannot be null!");
    }
    
    @Test
    public void getAllGamesByGenreEmptyTest(){
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
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> gameService.getAllGamesByGenre(""));
        Assertions.assertEquals(exception.getMessage(), "Genre cannot be an empty string!");
    }
    
    @Test
    public void getAllGamesByGenreInvalidTest(){
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
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        Game game_3 =
                new Game("different_one",
                        "description for title_1",
                        10.2,
                        developersPublisher2.get(0),
                        publishers.get(1),
                        Arrays.stream((new Genre[]{Genre.ACTION, Genre.ADVENTURE})).collect(Collectors.toList()));
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> gameService.getAllGamesByGenre("Invalid"));
        Assertions.assertEquals(exception.getMessage(), "No enum constant mk.com.store.games.gamestore.model.enumeration.Genre.Invalid");
    }
    
    @Test
    public void getAllGamesByGenreValidNoGamesTest(){
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
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        Assertions.assertEquals( result, gameService.getAllGamesByGenre("JRPG"));
    }
    
    @Test
    public void getAllGamesByGenreValidOneGamesTest(){
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
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        result.add(game_3);
        Assertions.assertEquals( result, gameService.getAllGamesByGenre("RPG"));
    }
    
    @Test
    public void getAllGamesByGenreValidMultipleGamesTest(){
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
        List<Game> result = new ArrayList<>();
        mongoTemplate.save(game_1);
        mongoTemplate.save(game_2);
        mongoTemplate.save(game_3);
        result.add(game_1);
        result.add(game_2);
        Assertions.assertEquals( result, gameService.getAllGamesByGenre("ACTION"));
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

