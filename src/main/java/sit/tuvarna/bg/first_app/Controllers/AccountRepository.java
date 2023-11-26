package sit.tuvarna.bg.first_app.Controllers;
import org.springframework.data.jpa.repository.JpaRepository;
import sit.tuvarna.bg.first_app.Tables.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);

    boolean existsByUsername(String username);
}
