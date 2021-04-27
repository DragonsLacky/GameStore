package mk.com.store.games.gamestore.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import mk.com.store.games.gamestore.model.Developer;
import mk.com.store.games.gamestore.model.dto.DeveloperDto;
import mk.com.store.games.gamestore.model.exception.DeveloperNotFoundException;
import mk.com.store.games.gamestore.model.exception.PublisherNotFoundException;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;
import mk.com.store.games.gamestore.service.DeveloperService;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/dev")
public class DeveloperController {

    private final DeveloperService developerService;
    

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }


    @GetMapping
    public List<Developer> getAllDevs() {
        return developerService.getAllDevelopers();
    }

    @GetMapping("/{publisherId}")
    public List<Developer> getAllDevsForPublisher(@PathVariable String publisherId) throws UserNotFoundException, PublisherNotFoundException{
        return developerService.getAllDevelopersForPublisher("admin2", publisherId);
    }

    @PreAuthorize("hasRole('PUBLISHER') or hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Developer> addDev(@RequestBody DeveloperDto developerDto) throws PublisherNotFoundException, UserNotFoundException {
        return developerService.addNewDeveloper(developerDto)
            .map(dev -> ResponseEntity.ok().body(dev))
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PreAuthorize("hasRole('PUBLISHER') or hasRole('ADMIN')")
    @PutMapping("/edit/{developerId}")
    public ResponseEntity<Developer> editDev(@PathVariable String developerId, @RequestBody DeveloperDto developerDto) throws PublisherNotFoundException, UserNotFoundException, DeveloperNotFoundException {
        return developerService.editDeveloper(developerId,developerDto)
            .map(dev -> ResponseEntity.ok().body(dev))
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }
    
    @PreAuthorize("hasRole('PUBLISHER') or hasRole('ADMIN')")
    @DeleteMapping("/remove/{developerId}")
    public ResponseEntity<Boolean> deleteDev(@PathVariable String developerId) throws PublisherNotFoundException, UserNotFoundException, DeveloperNotFoundException {
        return developerService.removeDeveloper(developerId)
            .map(bool -> ResponseEntity.ok().body(bool))
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

}