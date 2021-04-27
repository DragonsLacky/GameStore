package mk.com.store.games.gamestore.web.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mk.com.store.games.gamestore.config.security.JwtUtils;
import mk.com.store.games.gamestore.model.Role;
import mk.com.store.games.gamestore.model.dto.JwtResponse;
import mk.com.store.games.gamestore.model.dto.LoginRequest;
import mk.com.store.games.gamestore.model.dto.RegisterRequest;
import mk.com.store.games.gamestore.model.dto.UserDto;
import mk.com.store.games.gamestore.model.enumeration.ERole;
import mk.com.store.games.gamestore.repository.RoleRepository;
import mk.com.store.games.gamestore.service.UserService;
import mk.com.store.games.gamestore.service.inplementation.UserDetailsImplementation;

@CrossOrigin(origins = "*", maxAge=3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final JwtUtils jwtUtils;


    public AuthController(AuthenticationManager authenticationManager, UserService userService,
            RoleRepository roleRepository, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@RequestBody LoginRequest LoginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(LoginRequest.getUsername(), LoginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(role -> role.getAuthority()).collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt, 
                                                 userDetails.getId(),
                                                 userDetails.getUsername(),
                                                 userDetails.getEmail(),
                                                 roles
                                                 ));
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody RegisterRequest registerRequest) {
        if(userService.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().build();
        }

        if(userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        List<String> strRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role User is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role admin is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_PUBLISHER)
							.orElseThrow(() -> new RuntimeException("Error: Role publisher is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}
        
        return userService.register(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail(), roles)
                            .map(user -> new UserDto(user.getUsername(), user.getEmail(), user.getGames(), user.getPublishers(), user.getRoles(), user.getCart(), user.getWishlist()))
                            // .map(user -> new UserDto(user.getUsername(), user.getEmail(), user.getGames(), user.getPublishers(), user.getRoles(), user.getCart(), user.getWishlist()))
                            .map(user -> ResponseEntity.ok().body(user))
                            .orElseGet(() -> ResponseEntity.badRequest().build());

        
    }

}