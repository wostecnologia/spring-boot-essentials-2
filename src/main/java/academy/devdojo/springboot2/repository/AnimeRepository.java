package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimeRepository extends JpaRepository<Anime, Long> {

    Optional<Anime> findByName(String name);
}
