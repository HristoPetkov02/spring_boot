package sit.tuvarna.bg.first_app.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sit.tuvarna.bg.first_app.repository.AccountRepository;
import sit.tuvarna.bg.first_app.users.Account;

import java.util.Optional;


@Service
public class AccountDetailsService implements UserDetailsService {


    @Autowired
    private AccountRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = Optional.ofNullable(repository.findByUsername(username));
        return account.map(AccountInfoDetails::new).orElseThrow(() ->new UsernameNotFoundException("User doesn't exist"));
    }

}
