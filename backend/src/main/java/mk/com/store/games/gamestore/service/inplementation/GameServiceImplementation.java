package mk.com.store.games.gamestore.service.inplementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import mk.com.store.games.gamestore.model.Cart;
import mk.com.store.games.gamestore.model.Developer;
import mk.com.store.games.gamestore.model.Game;
import mk.com.store.games.gamestore.model.Publisher;
import mk.com.store.games.gamestore.model.User;
import mk.com.store.games.gamestore.model.Wishlist;
import mk.com.store.games.gamestore.model.dto.GameDto;
import mk.com.store.games.gamestore.model.enumeration.Genre;
import mk.com.store.games.gamestore.model.exception.DeveloperNotFoundException;
import mk.com.store.games.gamestore.model.exception.GameNotFoundException;
import mk.com.store.games.gamestore.model.exception.PublisherNotFoundException;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;
import mk.com.store.games.gamestore.repository.CartRepository;
import mk.com.store.games.gamestore.repository.DeveloperRepository;
import mk.com.store.games.gamestore.repository.GameRepository;
import mk.com.store.games.gamestore.repository.PublisherRepository;
import mk.com.store.games.gamestore.repository.UserRepository;
import mk.com.store.games.gamestore.repository.WishlistRepository;
import mk.com.store.games.gamestore.service.GameService;

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
    public Optional<Game> addGame(GameDto gameDto) throws PublisherNotFoundException, DeveloperNotFoundException {
        User user = userRepository.findByUsername(gameDto.getUsername()).stream().findFirst().orElseThrow();
        Publisher publisher = publisherRepository.findById(gameDto.getPublisherId()).orElseThrow(PublisherNotFoundException::new);
        Developer dev = publisher.getStudios().stream().filter(developer -> developer.getId().equals(gameDto.getDeveloperId())).findAny().orElseThrow(DeveloperNotFoundException::new);
        List<Genre> mappedGenres = gameDto.getGenres().stream().map(Genre::valueOf).collect(Collectors.toList());
        Game game = new Game(gameDto.getTitle(), gameDto.getDescription(), gameDto.getPrice(), dev, publisher, mappedGenres);
        game = gameRepository.save(game);
        user.getGames().add(game);
        developerRepository.save(dev);
        userRepository.save(user);
        return Optional.of(game);
    }
    
    @Override
    public List<Game> getAllGamesByUser(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username).stream().findFirst().orElseThrow(UserNotFoundException::new);
        return user.getGames();
    }

    @Override
    public List<Game> getAllCreatedGamesByUser(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username).stream().findFirst().orElseThrow(UserNotFoundException::new);
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
        return gameRepository.findByDeveloper(developerRepository.findById(devId).orElseThrow(DeveloperNotFoundException::new));
    }

    

    @Override
    public Optional<Boolean> removeGame(String id) throws GameNotFoundException {
        Game game = gameRepository.findById(id).orElseThrow(GameNotFoundException::new);
        List<User> users = userRepository.findByGames(game).stream().map((user) -> {
            user.getGames().remove(game);
            return user;
        }).collect(Collectors.toList());
        userRepository.saveAll(users);

        List<Cart> carts = cartRepository.findByGames(game).stream().map((cart) -> {
            cart.getGames().remove(game);
            return cart;
        }).collect(Collectors.toList());
        cartRepository.saveAll(carts);
        
        List<Wishlist> wishlists = wishlistRepository.findByGames(game).stream().map((wishlist) -> {
            wishlist.getGames().remove(game);
            return wishlist;
        }).collect(Collectors.toList());
        wishlistRepository.saveAll(wishlists);

        gameRepository.delete(game);
        return Optional.of(true);
    }

    @Override
    public List<Game> getAllGamesByGenre(String genre) {
        return gameRepository.findByGenres(Genre.valueOf(genre));
    }

    @Override
    public Optional<Game> editGame(String gameId, GameDto gameDto) throws GameNotFoundException, DeveloperNotFoundException, PublisherNotFoundException {
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);
        game.setTitle(gameDto.getTitle());
        game.setDescription(gameDto.getDescription());
        game.setPrice(gameDto.getPrice());
        game.setPublisher(publisherRepository.findById(gameDto.getPublisherId()).orElseThrow(PublisherNotFoundException::new));
        game.setDeveloper(developerRepository.findById(gameDto.getDeveloperId()).orElseThrow(DeveloperNotFoundException::new));
        game.setGenres(gameDto.getGenres().stream().map(genre -> Genre.valueOf(genre)).collect(Collectors.toList()));
        gameRepository.save(game);
        return Optional.of(game);
    }

}