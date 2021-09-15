package mk.com.store.games.gamestore.service.inplementation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import mk.com.store.games.gamestore.model.Publisher;
import mk.com.store.games.gamestore.model.User;
import mk.com.store.games.gamestore.model.dto.PublisherDto;
import mk.com.store.games.gamestore.model.exception.DeveloperNotFoundException;
import mk.com.store.games.gamestore.model.exception.PublisherNotFoundException;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;
import mk.com.store.games.gamestore.repository.GameRepository;
import mk.com.store.games.gamestore.repository.PublisherRepository;
import mk.com.store.games.gamestore.repository.UserRepository;
import mk.com.store.games.gamestore.service.DeveloperService;
import mk.com.store.games.gamestore.service.GameService;
import mk.com.store.games.gamestore.service.PublisherService;

@Service
public class PublisherServiceImplementation  implements PublisherService{

    private final PublisherRepository publisherRepository;
    private final UserRepository userRepository;
    private final DeveloperService developerService;


    public PublisherServiceImplementation(PublisherRepository publisherRepository, UserRepository userRepository,
            DeveloperService developerService) {
        this.publisherRepository = publisherRepository;
        this.userRepository = userRepository;
        this.developerService = developerService;
    }

    @Override
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    @Override
    public List<Publisher> getAllPublisherByUser(String username) throws UserNotFoundException {
        if(username == null) throw new IllegalArgumentException("Username can not be null");
        if(username.trim().equals("")) throw new IllegalArgumentException("Username can not be empty string");
        if(username.contains(" ")) throw new IllegalArgumentException("Username can not contain empty spaces");
        User user = userRepository.findByUsername(username).stream().findFirst().orElseThrow(UserNotFoundException::new);
        return user.getPublishers();
    }

    @Override
    public Optional<Publisher> addPublisher(PublisherDto publisherDto) throws UserNotFoundException {
        if(publisherDto == null) throw new IllegalArgumentException("Publisher info can not be null");
        if(publisherDto.getUsername() == null) throw new IllegalArgumentException("Username can not be null");
        if(publisherDto.getUsername().trim().equals("")) throw new IllegalArgumentException("Username can not be empty string");
        if(publisherDto.getUsername().contains(" ")) throw new IllegalArgumentException("Username can not contain empty spaces");
        User user = userRepository.findByUsername(publisherDto.getUsername()).stream().findFirst().orElseThrow(UserNotFoundException::new);
        Publisher publisher = new Publisher(publisherDto.getName(), publisherDto.getDescription());
        publisherRepository.save(publisher);
        user.getPublishers().add(publisher);
        userRepository.save(user);
        return Optional.of(publisher);
    }

    @Override
    public Optional<Publisher> editPublisher(String publisherId, PublisherDto publisherDto) throws PublisherNotFoundException {
        if(publisherDto == null) throw new IllegalArgumentException("Publisher info can not be null");
        if(publisherId == null) throw new IllegalArgumentException("PublisherId can not be null");
        if(publisherId.trim().equals("")) throw new IllegalArgumentException("publisherId can not be empty string");
        if(publisherId.contains(" ")) throw new IllegalArgumentException("publisherId can not contain empty spaces");
        Publisher publisher = publisherRepository.findById(publisherId).orElseThrow(PublisherNotFoundException::new);
        publisher.setName(publisherDto.getName());
        publisher.setDescription(publisherDto.getDescription());
        return Optional.of(publisherRepository.save(publisher));
    }

    @Override
    public Optional<Boolean> removePublisher(String publisherId) throws PublisherNotFoundException {
        if(publisherId == null) throw new IllegalArgumentException("PublisherId can not be null");
        if(publisherId.trim().equals("")) throw new IllegalArgumentException("PublisherId can not be empty string");
        if(publisherId.contains(" ")) throw new IllegalArgumentException("PublisherId can not contain empty spaces");
        Publisher publisher = publisherRepository.findById(publisherId).orElseThrow(PublisherNotFoundException::new);
        publisher.getStudios().forEach(studio -> {
            try {
                developerService.removeDeveloper(studio.getId());
            } catch (DeveloperNotFoundException | UserNotFoundException | PublisherNotFoundException e) {
                e.printStackTrace();
            }
        });
        publisherRepository.delete(publisher);
        return Optional.of(true);
    }
    
}