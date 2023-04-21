package academy.devdojo.springbootessentials.repository;

import academy.devdojo.springbootessentials.domain.Cartoon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartoonRepository extends JpaRepository<Cartoon, Long> {
    List<Cartoon> findByName(String name);
}
