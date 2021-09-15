package mk.com.store.games.gamestore.service;

import java.util.List;
import java.util.Optional;

import mk.com.store.games.gamestore.model.Publisher;
import mk.com.store.games.gamestore.model.dto.PublisherDto;
import mk.com.store.games.gamestore.model.exception.PublisherNotFoundException;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;

public interface PublisherService {

    public List<Publisher> getAllPublishers();

    public List<Publisher> getAllPublisherByUser(String username) throws UserNotFoundException;

    public Optional<Publisher> addPublisher(PublisherDto publisherDto) throws UserNotFoundException;

    public Optional<Publisher> editPublisher(String publisherId, PublisherDto publisherDto) throws UserNotFoundException, PublisherNotFoundException;

    public Optional<Boolean> removePublisher(String publisherId) throws UserNotFoundException, PublisherNotFoundException;

}