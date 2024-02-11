package sit.tuvarna.bg.first_app.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.tuvarna.bg.first_app.config.JwtService;
import sit.tuvarna.bg.first_app.repositories.UserRepository;
import sit.tuvarna.bg.first_app.users.Role;
import sit.tuvarna.bg.first_app.users.User;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private final UserRepository repository;
    @Autowired
    private final PasswordEncoder encoder;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
                  request.getUsername(),
                  request.getPassword()
          )
        );
        var user = repository.findByUsername(request.getUsername()).orElseThrow(() -> new NoSuchElementException("User not found"));

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
