package mk.com.store.games.gamestore.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mk.com.store.games.gamestore.model.User;
import mk.com.store.games.gamestore.model.dto.UserSearchDto;
import mk.com.store.games.gamestore.model.exception.UserNotFoundException;
import mk.com.store.games.gamestore.service.UserService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/{username}")
    public ResponseEntity<User> findUser(@RequestBody UserSearchDto userSearchDto) throws UserNotFoundException {
        return userService.getUser(userSearchDto.getUsername())
            .map(user -> ResponseEntity.ok().body(user))
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}