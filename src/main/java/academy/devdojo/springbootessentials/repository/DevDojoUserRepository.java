package academy.devdojo.springbootessentials.repository;

import academy.devdojo.springbootessentials.domain.Cartoon;
import academy.devdojo.springbootessentials.domain.DevDojoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DevDojoUserRepository extends JpaRepository<DevDojoUser, Long> {
    DevDojoUser findByUsername (String username);
}
