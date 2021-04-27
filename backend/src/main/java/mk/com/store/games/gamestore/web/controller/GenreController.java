package mk.com.store.games.gamestore.web.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import mk.com.store.games.gamestore.model.enumeration.Genre;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/genre")
public class GenreController {
    
    @GetMapping
    public List<Genre> getAll(){
        return List.of(Genre.values());
    }
}