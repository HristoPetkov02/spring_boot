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


}
