package sit.tuvarna.bg.first_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sit.tuvarna.bg.first_app.auth.AuthenticationRequest;
import sit.tuvarna.bg.first_app.auth.AuthenticationResponse;
import sit.tuvarna.bg.first_app.auth.AuthenticationService;
import sit.tuvarna.bg.first_app.auth.RegisterRequest;
import sit.tuvarna.bg.first_app.repositories.UserRepository;
import sit.tuvarna.bg.first_app.users.Role;
import sit.tuvarna.bg.first_app.users.User;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthenticationService service;

    @PostMapping("/auth")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        //login
        return ResponseEntity.ok(service.authenticate(request));
    }
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        //check if username is taken
        if (repository.findByUsername(request.getUsername()).isPresent())
            //return 404 if username is taken with body "Username is taken"
            return ResponseEntity.status(404).body(AuthenticationResponse.builder().token("Username is taken").build());
        return ResponseEntity.ok(service.register(request));
    }


    @GetMapping("/single")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Object> getDetails(){
        return ResponseEntity.ok(repository.findByUsername(Objects.requireNonNull(getLoggedInUserDetails()).getUsername()));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getAllAccounts(){
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping("/grantAuthority")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> grantAuthority(@RequestBody Map<String, String> requestMap){
        String username = requestMap.get("username");
        User user = repository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("User not found"));

        if (user == null)
            return ResponseEntity.status(404).body("Account is missing");
        user.setRole(Role.ADMIN);
        repository.save(user);
        return ResponseEntity.ok("Granted authority to "+ username);
    }

    @PostMapping("/revokeAuthority")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> revokeAuthority(@RequestBody Map<String, String>  requestMap){
        String username = requestMap.get("username");
        User user = repository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("User not found"));

        if (user == null)
            return ResponseEntity.status(404).body("Account is missing");
        user.setRole(Role.USER);
        repository.save(user);
        return ResponseEntity.ok("Revoked authority to "+ username);
    }

    private UserDetails getLoggedInUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof  UserDetails)
            return (UserDetails) authentication.getPrincipal();
        return null;
    }
}
