package sit.tuvarna.bg.first_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import sit.tuvarna.bg.first_app.repositories.AccountRepository;
import sit.tuvarna.bg.first_app.users.Account;

import java.util.Objects;

@RestController
public class AccountController {
    @Autowired
    private AccountRepository repository;

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
    public ResponseEntity<Object> saveAccount (@RequestBody Account account){
        if (repository.existsByUsername(account.getUsername()))
            return ResponseEntity.status(404).body("This username has been taken");

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setRole("USER");//винаги в началото да е USER
        Account result= repository.save(account);

        if(result.getId_account()>0){
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
    public ResponseEntity<Object> getAllAccounts(){
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping("/account/grantAuthority")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> grantAuthority(String username){
        Account account = repository.findByUsername(username);

        if (account == null)
            return ResponseEntity.status(404).body("Account is missing");
        account.setRole("ADMIN");
        repository.save(account);
        return ResponseEntity.ok("Granted authority to "+ username);
    }

    @PostMapping("/account/revokeAuthority")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> revokeAuthority(String username){
        Account account = repository.findByUsername(username);

        if (account == null)
            return ResponseEntity.status(404).body("Account is missing");
        account.setRole("USER");
        repository.save(account);
        return ResponseEntity.ok("Revoked authority to "+ username);
    }

    private UserDetails getLoggedInUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof  UserDetails)
            return (UserDetails) authentication.getPrincipal();
        return null;
    }
}
