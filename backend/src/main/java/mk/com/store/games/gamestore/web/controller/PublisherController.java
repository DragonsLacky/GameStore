package mk.com.store.games.gamestore.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mk.com.store.games.gamestore.model.Publisher;
import mk.com.store.games.gamestore.model.dto.PublisherDto;
import mk.com.store.games.gamestore.model.exception.PublisherNotFoundException;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;
import mk.com.store.games.gamestore.service.PublisherService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/publisher")
public class PublisherController {
    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public List<Publisher> gList() {
        return this.publisherService.getAllPublishers();
    }
    
    // TODO: make addEditor link
    // TODO: make removeEditor link

    @GetMapping("/{username}")
    public List<Publisher> getUserPublishers(@PathVariable String username) throws UserNotFoundException {
        return this.publisherService.getAllPublisherByUser(username);
    }
    
    @PostMapping("/add")
    public ResponseEntity<Publisher> addPublisher(@RequestBody PublisherDto publisherDto) throws UserNotFoundException {
        return publisherService.addPublisher(publisherDto)
            .map(publisher -> ResponseEntity.ok().body(publisher))
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/edit/{publisherId}")
    public ResponseEntity<Publisher> editPublisher(@PathVariable String publisherId, @RequestBody PublisherDto publisherDto) throws UserNotFoundException, PublisherNotFoundException {
        return publisherService.editPublisher(publisherId, publisherDto)
            .map(publisher -> ResponseEntity.ok().body(publisher))
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/edit/{publisherId}")
    public ResponseEntity<Boolean> deletePublisher(@PathVariable String publisherId, @RequestBody String username) throws UserNotFoundException, PublisherNotFoundException {
        return publisherService.removePublisher(publisherId, username)
            .map(bool -> ResponseEntity.ok().body(bool))
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}