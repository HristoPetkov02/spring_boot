package sit.tuvarna.bg.first_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sit.tuvarna.bg.first_app.repository.AccountRepository;
import sit.tuvarna.bg.first_app.users.Account;

@RestController
@RequestMapping
public class test {
    @Autowired
    private AccountRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home(){
        return "itworks";
    }

    @PostMapping("/account/save")
    public ResponseEntity<Object> saveAccount (@RequestBody Account account){
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        Account result= repository.save(account);

        if(result.getId_account()>0){
            return ResponseEntity.ok("Account was saved");
        }
        return ResponseEntity.status(404).body("Error! Account was not saved.");
    }

    @GetMapping("/account/single")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Object> getDetails(){
        return ResponseEntity.ok(repository.findByUsername(getLoggedInUserDetails().getUsername()));
    }

    @GetMapping("/account/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getAllAccounts(){
        return ResponseEntity.ok(repository.findAll());
    }

    private UserDetails getLoggedInUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof  UserDetails)
            return (UserDetails) authentication.getPrincipal();
        return null;
    }
}
