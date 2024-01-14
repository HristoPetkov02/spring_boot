package sit.tuvarna.bg.first_app.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sit.tuvarna.bg.first_app.config.UserInfoDetails;
import sit.tuvarna.bg.first_app.repositories.UserRepository;
import sit.tuvarna.bg.first_app.users.User;

import java.util.Optional;


@Service
public class AccountDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> account = repository.findByUsername(username);
        return account.map(UserInfoDetails::new).orElseThrow(() ->new UsernameNotFoundException("User doesn't exist"));
    }

}
