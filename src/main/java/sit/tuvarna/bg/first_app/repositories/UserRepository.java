package sit.tuvarna.bg.first_app.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.tuvarna.bg.first_app.users.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    //?1 индикира че ще има параметър и че за сега е placeholder
    @Query(value="SELECT * FROM Users WHERE username = ?1",nativeQuery = true)
    //Optional<User> findByUsername(String username);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
