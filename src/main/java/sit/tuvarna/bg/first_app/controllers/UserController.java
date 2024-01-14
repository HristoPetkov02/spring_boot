package sit.tuvarna.bg.first_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sit.tuvarna.bg.first_app.repositories.UserRepository;
import sit.tuvarna.bg.first_app.users.Role;
import sit.tuvarna.bg.first_app.users.User;

import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
public class UserController {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*@PostMapping("/logIn")
    @CrossOrigin(origins = "http://localhost:3000")
    public String login(@RequestBody LogInRequest logInRequest){
        Account account = accountRepository.findByUsername(logInRequest.getUsername());
        if (account != null && account.getPassword().equals(logInRequest.getPassword())) {
            if (account.getRole().equals("ADMIN")) {
                return "Login successful. User is an admin.";
            } else {
                return "Login successful. User is not an admin.";
            }
        } else {
            return "Login failed. Invalid username or password.";
        }
    }


    @PostMapping("/createAccount")
    public String createAccount(@RequestBody Account account) {
        if (accountRepository.existsByUsername(account.getUsername()))
            return "This username has been taken";
        accountRepository.save(account);
        return "Account created successfully.";
    }*/

    @PostMapping("/account/save")
    public ResponseEntity<Object> saveAccount (@RequestBody User user){
        if (repository.existsByUsername(user.getUsername()))
            return ResponseEntity.status(404).body("This username has been taken");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);//винаги в началото да е USER
        User result= repository.save(user);

        if(result.getId_user()>0){
            return ResponseEntity.ok("Account was saved");
        }
        return ResponseEntity.status(404).body("Error! Account was not saved.");
    }

    @GetMapping("/account/single")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Object> getDetails(){
        return ResponseEntity.ok(repository.findByUsername(Objects.requireNonNull(getLoggedInUserDetails()).getUsername()));
    }

    @GetMapping("/account/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    //@CrossOrigin(origins = "*")
    @CrossOrigin(origins = "http://localhost:3000//account//all")
    public ResponseEntity<Object> getAllAccounts(){
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping("/account/grantAuthority")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> grantAuthority(String username){
        User user = repository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("User not found"));

        if (user == null)
            return ResponseEntity.status(404).body("Account is missing");
        user.setRole(Role.ADMIN);
        repository.save(user);
        return ResponseEntity.ok("Granted authority to "+ username);
    }

    @PostMapping("/account/revokeAuthority")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> revokeAuthority(String username){
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
