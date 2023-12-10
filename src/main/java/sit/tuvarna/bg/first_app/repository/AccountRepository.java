package sit.tuvarna.bg.first_app.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.tuvarna.bg.first_app.users.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    //?1 индикира че ще има параметър и че за сега е placeholder
    @Query(value="SELECT * FROM Accounts WHERE username = ?1",nativeQuery = true)
    Account findByUsername(String username);

    boolean existsByUsername(String username);
}
