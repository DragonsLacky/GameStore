package mk.com.store.games.gamestore.service.inplementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mk.com.store.games.gamestore.model.*;
import mk.com.store.games.gamestore.model.enumeration.ERole;
import mk.com.store.games.gamestore.model.exception.*;
import org.springframework.stereotype.Service;

import mk.com.store.games.gamestore.model.dto.GameDto;
import mk.com.store.games.gamestore.model.enumeration.Genre;
import mk.com.store.games.gamestore.repository.CartRepository;
import mk.com.store.games.gamestore.repository.DeveloperRepository;
import mk.com.store.games.gamestore.repository.GameRepository;
import mk.com.store.games.gamestore.repository.PublisherRepository;
import mk.com.store.games.gamestore.repository.UserRepository;
import mk.com.store.games.gamestore.repository.WishlistRepository;
import mk.com.store.games.gamestore.service.GameService;

import javax.swing.text.Caret;

@Service
public class GameServiceImplementation implements GameService{

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final PublisherRepository publisherRepository;
    private final DeveloperRepository developerRepository;
    private final CartRepository cartRepository;
    private final WishlistRepository wishlistRepository;

    public GameServiceImplementation(GameRepository gameRepository, UserRepository userRepository,
            PublisherRepository publisherRepository, DeveloperRepository developerRepository,
            CartRepository cartRepository, WishlistRepository wishlistRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.publisherRepository = publisherRepository;
        this.developerRepository = developerRepository;
        this.cartRepository = cartRepository;
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }
    
    @Override
    public List<Game> searchByTitle(String term) throws IllegalArgumentException {
        if(term == null) throw new IllegalArgumentException("Search term is null");
        return gameRepository.findAllByTitleContainingIgnoreCase(term);
    }
    
    @Override
    public Optional<Game> addGame(GameDto gameDto) throws PublisherNotFoundException, DeveloperNotFoundException, DeveloperNotContainedInPublisherException, UserNotFoundException {
        if(gameDto == null) throw new IllegalArgumentException("Game information is missing");
        if(gameDto.getUsername()== null) throw new IllegalArgumentException("Username can not be null!");
        if(gameDto.getUsername().trim().equals("")) throw new IllegalArgumentException("Username can not be empty string!");
        if(gameDto.getUsername().contains(" ")) throw new IllegalArgumentException("Username can not have any empty spaces!");
        User user = userRepository.findByUsername(gameDto.getUsername()).stream().findFirst().orElseThrow(UserNotFoundException::new);
        if(gameDto.getPublisherId() == null) throw new IllegalArgumentException("PublisherId can not be null!");
        if(gameDto.getPublisherId().trim().equals("")) throw new IllegalArgumentException("PublisherId can not be empty string!");
        Publisher publisher = publisherRepository.findById(gameDto.getPublisherId()).orElseThrow(PublisherNotFoundException::new);
        if(gameDto.getDeveloperId() == null) throw new IllegalArgumentException("DeveloperId can not be null!");
        if(gameDto.getDeveloperId().trim().equals("")) throw new IllegalArgumentException("DeveloperId can not be empty string!");
        Developer dev = publisher.getStudios().stream().filter(developer -> developer.getId().equals(gameDto.getDeveloperId())).findAny().orElseThrow(DeveloperNotFoundException::new);
        List<Genre> mappedGenres = gameDto.getGenres().stream().map(Genre::valueOf).collect(Collectors.toList());
        Game game = new Game(gameDto.getTitle(), gameDto.getDescription(), gameDto.getPrice(), dev, publisher, mappedGenres);
        gameRepository.save(game);
        user.getGames().add(game);
        developerRepository.save(dev);
        userRepository.save(user);
        return Optional.of(game);
    }
    
    @Override
    public List<Game> getAllGamesByUser(String username) throws UserNotFoundException {
        if(username == null) throw new IllegalArgumentException("Username can not be null!");
        if(username.trim().equals("")) throw new IllegalArgumentException("Username can not be empty string!");
        if(username.contains(" ")) throw new IllegalArgumentException("Username can not have any empty spaces!");
        User user = userRepository.findByUsername(username).stream().findFirst().orElseThrow(UserNotFoundException::new);
        return user.getGames();
    }

    @Override
    public List<Game> getAllCreatedGamesByUser(String username) throws UserNotFoundException, PublisherNotFoundException {
        if(username == null) throw new IllegalArgumentException("Username can not be null!");
        if(username.trim().equals("")) throw new IllegalArgumentException("Username can not be empty string!");
        if(username.contains(" ")) throw new IllegalArgumentException("Username can not have any empty spaces!");
        User user = userRepository.findByUsername(username).stream().findFirst().orElseThrow(UserNotFoundException::new);
        if (user.getRoles().stream().noneMatch(role -> role.getName() == ERole.ROLE_PUBLISHER)) throw new PublisherNotFoundException();
        List<Publisher> publishers = user.getPublishers();
        return publishers.stream().flatMap(publisher -> publisher.getStudios().stream()).flatMap(studio -> {
            try {
                return getAllGamesByDev(studio.getId()).stream();
            } catch (DeveloperNotFoundException e) {
                e.printStackTrace();
            }
            return Stream.of();
        }).collect(Collectors.toList());

    }
    
    @Override
    public List<Game> getAllGamesByDev(String devId) throws DeveloperNotFoundException {
        if(devId == null) throw new IllegalArgumentException("Id cannot be null!");
        return gameRepository.findByDeveloper(developerRepository.findById(devId).orElseThrow(DeveloperNotFoundException::new));
    }

    

    @Override
    public Optional<Boolean> removeGame(String id) throws GameNotFoundException {
        if(id == null) throw new IllegalArgumentException("Id cannot be null!");
        if(id.trim().equals("")) throw new IllegalArgumentException("Id cannot be empty string!");
        Game game = gameRepository.findById(id).orElseThrow(GameNotFoundException::new);
        List<User> users = userRepository.findByGames(game).stream().peek((user) -> user.getGames().remove(game)).collect(Collectors.toList());
        userRepository.saveAll(users);

        List<Cart> carts = cartRepository.findByGames(game).stream().peek((cart) -> cart.getGames().remove(game)).collect(Collectors.toList());
        cartRepository.saveAll(carts);
        
        List<Wishlist> wishlists = wishlistRepository.findByGames(game).stream().peek((wishlist) -> wishlist.getGames().remove(game)).collect(Collectors.toList());
        wishlistRepository.saveAll(wishlists);

        gameRepository.delete(game);
        return Optional.of(true);
    }

    @Override
    public List<Game> getAllGamesByGenre(String genre) {
        if(genre == null) throw new IllegalArgumentException("Genre cannot be null!");
        if(genre.equals("")) throw new IllegalArgumentException("Genre cannot be an empty string!");
        return gameRepository.findByGenres(Genre.valueOf(genre));
    }

    @Override
    public Optional<Game> editGame(String gameId, GameDto gameDto) throws GameNotFoundException, DeveloperNotFoundException, PublisherNotFoundException, DeveloperNotContainedInPublisherException {
        if(gameId == null) throw new IllegalArgumentException("GameId can not be null!");
        if(gameId.trim().equals("")) throw new IllegalArgumentException("GameId can not be empty string!");
        if(gameDto == null) throw new IllegalArgumentException("Game information is missing");
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);
        Publisher publisher = publisherRepository.findById(gameDto.getPublisherId()).orElseThrow(PublisherNotFoundException::new);
        Developer developer = developerRepository.findById(gameDto.getDeveloperId()).orElseThrow(DeveloperNotFoundException::new);
        if(!publisher.getStudios().contains(developer)) throw new DeveloperNotContainedInPublisherException();
        game.setTitle(gameDto.getTitle());
        game.setDescription(gameDto.getDescription());
        game.setPrice(gameDto.getPrice());
        game.setPublisher(publisher);
        game.setDeveloper(developer);
        game.setGenres(gameDto.getGenres().stream().map(Genre::valueOf).collect(Collectors.toList()));
        gameRepository.save(game);
        return Optional.of(game);
    }

}