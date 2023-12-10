package sit.tuvarna.bg.first_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sit.tuvarna.bg.first_app.LogInRequest;
import sit.tuvarna.bg.first_app.repository.AccountRepository;
import sit.tuvarna.bg.first_app.users.Account;

@RestController
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/logIn")
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
    }
}
