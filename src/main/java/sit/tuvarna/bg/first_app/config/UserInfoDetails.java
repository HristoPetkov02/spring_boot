package sit.tuvarna.bg.first_app.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sit.tuvarna.bg.first_app.users.User;

import java.util.Collection;


public class UserInfoDetails implements UserDetails {
    private String username;
    private String password;
    //private List<GrantedAuthority> roles;

    public UserInfoDetails(User user){
        this.username= user.getUsername();
        this.password= user.getPassword();
        //this.roles= Arrays.stream(user.getRole()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
