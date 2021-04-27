package mk.com.store.games.gamestore.service;

import java.util.List;
import java.util.Optional;

import mk.com.store.games.gamestore.model.Developer;
import mk.com.store.games.gamestore.model.dto.DeveloperDto;
import mk.com.store.games.gamestore.model.exception.DeveloperNotFoundException;
import mk.com.store.games.gamestore.model.exception.PublisherNotFoundException;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;

public interface DeveloperService {

    public List<Developer> getAllDevelopers();    

    public List<Developer> getAllDevelopersForPublisher(String username, String publisherId) throws UserNotFoundException, PublisherNotFoundException;

    public Optional<Developer> addNewDeveloper(DeveloperDto developerDto) throws PublisherNotFoundException, UserNotFoundException;

    public Optional<Developer> editDeveloper(String developerId, DeveloperDto developerDto) throws UserNotFoundException, PublisherNotFoundException, DeveloperNotFoundException;

    public Optional<Boolean> removeDeveloper(String developerId, DeveloperDto developerDto) throws DeveloperNotFoundException, UserNotFoundException, PublisherNotFoundException;
}