package sit.tuvarna.bg.first_app.Tables;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Objects;


@Entity
@Table(name="Accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_account;
    @Column(unique = true)
    private String username;
    private String email;
    private String password;
    private boolean administrator;


    public Account(){}

    public Account(String username, String email, String password, boolean administrator) {
        //this.id_account = id_account;
        this.username = username;
        this.email = email;
        this.password = password;
        this.administrator = administrator;
    }

    public int getId_account() {
        return id_account;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", administrator=" + administrator +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return username.equals(account.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
