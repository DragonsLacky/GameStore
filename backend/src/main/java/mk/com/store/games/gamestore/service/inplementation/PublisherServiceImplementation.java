package mk.com.store.games.gamestore.service.inplementation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import mk.com.store.games.gamestore.model.Publisher;
import mk.com.store.games.gamestore.model.User;
import mk.com.store.games.gamestore.model.dto.PublisherDto;
import mk.com.store.games.gamestore.model.exception.PublisherNotFoundException;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;
import mk.com.store.games.gamestore.repository.GameRepository;
import mk.com.store.games.gamestore.repository.PublisherRepository;
import mk.com.store.games.gamestore.repository.UserRepository;
import mk.com.store.games.gamestore.service.PublisherService;

@Service
public class PublisherServiceImplementation  implements PublisherService{

    private final PublisherRepository publisherRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;


    public PublisherServiceImplementation(PublisherRepository publisherRepository, UserRepository userRepository,
            GameRepository gameRepository) {
        this.publisherRepository = publisherRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    @Override
    public List<Publisher> getAllPublisherByUser(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username).stream().findFirst().orElseThrow(UserNotFoundException::new);
        return user.getPublishers();
    }

    @Override
    public Optional<Publisher> addPublisher(PublisherDto publisherDto) throws UserNotFoundException {
        User user = userRepository.findByUsername(publisherDto.getUsername()).stream().findFirst().orElseThrow(UserNotFoundException::new);
        Publisher publisher = new Publisher(publisherDto.getName(), publisherDto.getDescription());
        publisher = publisherRepository.save(publisher);
        user.getPublishers().add(publisher);
        userRepository.save(user);
        return Optional.of(publisher);
    }

    @Override
    public Optional<Publisher> addEditor(String publisherId, String username) throws UserNotFoundException, PublisherNotFoundException {
        User user = userRepository.findByUsername(username).stream().findFirst().orElseThrow(UserNotFoundException::new);
        Publisher publisher = publisherRepository.findById(publisherId).orElseThrow(PublisherNotFoundException::new);
        user.getPublishers().add(publisher);
        userRepository.save(user);
        return Optional.of(publisherRepository.save(publisher));
    }

    @Override
    public Optional<Publisher> removeEditor(String publisherId, String username) throws UserNotFoundException, PublisherNotFoundException {
        User user = userRepository.findByUsername(username).stream().findFirst().orElseThrow(UserNotFoundException::new);
        Publisher publisher = publisherRepository.findById(publisherId).orElseThrow(PublisherNotFoundException::new);
        user.getPublishers().remove(publisher);
        userRepository.save(user);
        return Optional.of(publisherRepository.save(publisher));
    }

    @Override
    public Optional<Publisher> editPublisher(String publisherId, PublisherDto publisherDto) throws UserNotFoundException, PublisherNotFoundException {
        User user = userRepository.findByUsername(publisherDto.getUsername()).stream().findFirst().orElseThrow(UserNotFoundException::new);
        Publisher publisher = user.getPublishers().stream().filter(pub -> pub.getId().equals(publisherId)).findFirst().orElseThrow(PublisherNotFoundException::new);
        publisher.setName(publisherDto.getName());
        publisher.setDescription(publisherDto.getDescription());
        return Optional.of(publisherRepository.save(publisher));
    }

    @Override
    public Optional<Boolean> removePublisher(String publisherId, String username) throws UserNotFoundException, PublisherNotFoundException {
        Publisher publisher = publisherRepository.findById(publisherId).stream().filter(pub -> pub.getId().equals(publisherId)).findFirst().orElseThrow(PublisherNotFoundException::new);
        if(publisher.getStudios().stream().anyMatch(studio -> !gameRepository.findByDeveloper(studio).isEmpty())){
            return Optional.of(false);
        }
        publisherRepository.delete(publisher);
        return Optional.of(true);
    }
    
}