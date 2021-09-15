package mk.com.store.games.gamestore.service.inplementation;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import mk.com.store.games.gamestore.model.Developer;
import mk.com.store.games.gamestore.model.Publisher;
import mk.com.store.games.gamestore.model.User;
import mk.com.store.games.gamestore.model.dto.DeveloperDto;
import mk.com.store.games.gamestore.model.exception.DeveloperNotFoundException;
import mk.com.store.games.gamestore.model.exception.GameNotFoundException;
import mk.com.store.games.gamestore.model.exception.PublisherNotFoundException;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;
import mk.com.store.games.gamestore.repository.DeveloperRepository;
import mk.com.store.games.gamestore.repository.GameRepository;
import mk.com.store.games.gamestore.repository.PublisherRepository;
import mk.com.store.games.gamestore.repository.UserRepository;
import mk.com.store.games.gamestore.service.DeveloperService;
import mk.com.store.games.gamestore.service.GameService;

@Service
public class DeveloperServiceImplementation implements DeveloperService {

    private final UserRepository userRepository;
    private final PublisherRepository publisherRepository;
    private final DeveloperRepository developerRepository;
    private final GameService gameService;

    public DeveloperServiceImplementation(UserRepository userRepository, PublisherRepository publisherRepository,
            DeveloperRepository developerRepository, GameService gameService) {
        this.userRepository = userRepository;
        this.publisherRepository = publisherRepository;
        this.developerRepository = developerRepository;
        this.gameService = gameService;
    }
    
    @Override
    public List<Developer> getAllDevelopers() {
        return developerRepository.findAll();
    }

    @Override
    public List<Developer> getAllDevelopersForPublisher(String username, String publisherId) throws UserNotFoundException, PublisherNotFoundException {
        if(username == null) throw new IllegalArgumentException("Username can not be null");
        if(username.trim().equals("")) throw new IllegalArgumentException("Username can not be empty string");
        if(username.contains(" ")) throw new IllegalArgumentException("Username can not contain empty spaces");
        if(publisherId == null) throw new IllegalArgumentException("PublisherId can not be null");
        if(publisherId.trim().equals("")) throw new IllegalArgumentException("PublisherId can not be empty string");
        if(publisherId.contains(" ")) throw new IllegalArgumentException("PublisherId can not contain empty spaces");
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        Publisher publisher = user.getPublishers().stream().filter(pub -> pub.getId().equals(publisherId)).findFirst().orElseThrow(PublisherNotFoundException::new);
        return publisher.getStudios();
    }

    @Override
    public Optional<Developer> addNewDeveloper(DeveloperDto developerDto) throws PublisherNotFoundException, UserNotFoundException {
        if(developerDto == null) throw new IllegalArgumentException("developer info can not be null");
        if(developerDto.getUsername() == null) throw new IllegalArgumentException("Username can not be null");
        if(developerDto.getUsername().trim().equals("")) throw new IllegalArgumentException("Username can not be empty string");
        if(developerDto.getUsername().contains(" ")) throw new IllegalArgumentException("Username can not contain empty spaces");
        User user = userRepository.findByUsername(developerDto.getUsername()).orElseThrow(UserNotFoundException::new);
        Publisher publisher = user.getPublishers().stream().filter(pub -> pub.getId().equals(developerDto.getPublisherId())).findFirst().orElseThrow(PublisherNotFoundException::new);
        Developer developer = new Developer(developerDto.getName());
        developerRepository.save(developer);
        publisher.getStudios().add(developer);
        publisherRepository.save(publisher);
        return Optional.of(developer);
    }

    @Override
    public Optional<Developer> editDeveloper(String developerId, DeveloperDto developerDto) throws DeveloperNotFoundException {
        if(developerDto == null) throw new IllegalArgumentException("Developer info can not be null");
        if(developerId == null) throw new IllegalArgumentException("DeveloperId can not be null");
        if(developerId.trim().equals("")) throw new IllegalArgumentException("DeveloperId can not be empty string");
        if(developerId.contains(" ")) throw new IllegalArgumentException("DeveloperId can not contain empty spaces");
        Developer developer = developerRepository.findById(developerId).orElseThrow(DeveloperNotFoundException::new);
        developer.setName(developerDto.getName());
        return Optional.of(developerRepository.save(developer));
    }

    @Override
    public Optional<Boolean> removeDeveloper(String developerId) throws DeveloperNotFoundException, UserNotFoundException, PublisherNotFoundException {
        if(developerId == null) throw new IllegalArgumentException("DeveloperId can not be null");
        if(developerId.trim().equals("")) throw new IllegalArgumentException("DeveloperId can not be empty string");
        if(developerId.contains(" ")) throw new IllegalArgumentException("DeveloperId can not contain empty spaces");
        Developer developer = developerRepository.findById(developerId).orElseThrow(DeveloperNotFoundException::new);
        Publisher publisher = publisherRepository.findByStudios(developer).orElseThrow(PublisherNotFoundException::new);
        gameService.getAllGamesByDev(developerId).forEach(game -> {
            try {
                gameService.removeGame(game.getId());
            } catch (GameNotFoundException e) {
                e.printStackTrace();
            }
        });
        publisher.getStudios().remove(developer);
        publisherRepository.save(publisher);
        developerRepository.delete(developer);
        return Optional.of(true);
    }
    
}