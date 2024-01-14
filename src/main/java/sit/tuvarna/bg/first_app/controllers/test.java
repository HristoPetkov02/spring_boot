package sit.tuvarna.bg.first_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sit.tuvarna.bg.first_app.LogInRequest;
import sit.tuvarna.bg.first_app.auth.AuthenticationRequest;
import sit.tuvarna.bg.first_app.auth.AuthenticationResponse;
import sit.tuvarna.bg.first_app.auth.AuthenticationService;
import sit.tuvarna.bg.first_app.auth.RegisterRequest;

@RestController
@RequestMapping("/test")
public class test {
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private AuthenticationService service;


    @GetMapping("/")
    public String home(){
        return "itworks";
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
            return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/auth")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        //login
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> login(@RequestBody LogInRequest logInRequest) {
        try {
            // Perform authentication
            Authentication authentication = authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(logInRequest.getUsername(), logInRequest.getPassword())
            );

            // Set authenticated user in SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Spring Security will handle session creation and management by default

            // You can perform additional logic here if needed (e.g., generate a token)

            // Retrieve the authenticated user
            String username = authentication.getName();

            return ResponseEntity.ok("Login successful. Welcome, " + username + "!");
        } catch (Exception e) {
            // Handle authentication failure
            return ResponseEntity.status(401).body("Login failed. Invalid credentials.");
        }
    }


    /*
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LogInRequest logInRequest) {
        try {
            // Debugging: Print input credentials
            System.out.println("Received login request for username: " + logInRequest.getUsername());

            // Perform authentication
            Authentication authentication = authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(logInRequest.getUsername(), logInRequest.getPassword())
            );

            // Debugging: Print authentication details
            System.out.println("Authentication successful for username: " + authentication.getName());

            // Set authenticated user in SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // You can perform additional logic here if needed (e.g., generate a token)

            // Retrieve the authenticated user
            String username = authentication.getName();

            return ResponseEntity.ok("Login successful. Welcome, " + username + "!");
        } catch (Exception e) {
            // Debugging: Print authentication failure details
            System.out.println("Authentication failed. Exception: " + e.getMessage());

            // Handle authentication failure
            return ResponseEntity.status(401).body("Login failed. Invalid credentials.");
        }
    }*/


}
