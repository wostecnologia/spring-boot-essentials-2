package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static academy.devdojo.springboot2.util.AnimeCreator.createAnimeToBeSaved;

@DataJpaTest
@DisplayName("Tests for Anime Repository")
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save creates anime when Successful")
    void save_PersistAnime_WhenSuccessful() {
        Anime animeToSaved = createAnimeToBeSaved();
        Anime animeSaved = this.animeRepository.save(animeToSaved);
        Assertions.assertThat(animeSaved).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToSaved.getName());
    }

    @Test
    @DisplayName("Save updates anime when Successful")
    void save_UpdateAnime_WhenSuccessful() {
        Anime animeToSaved = createAnimeToBeSaved();
        Anime animeSaved = this.animeRepository.save(animeToSaved);

        animeSaved.setName("Overlord");
        Anime animeUpdated = this.animeRepository.save(animeSaved);

        Assertions.assertThat(animeUpdated).isNotNull();
        Assertions.assertThat(animeUpdated.getId()).isNotNull();
        Assertions.assertThat(animeUpdated.getName()).isEqualTo(animeToSaved.getName());
    }

    @Test
    @DisplayName("Delete removes anime when Successful")
    void save_RemoveAnime_WhenSuccessful() {
        Anime animeToSaved = createAnimeToBeSaved();
        Anime animeSaved = this.animeRepository.save(animeToSaved);

        this.animeRepository.delete(animeSaved);
        Optional<Anime> animeOptional = this.animeRepository.findById(animeSaved.getId());

        Assertions.assertThat(animeOptional.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Find By Name returns list of anime when Successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful(){
        Anime animeToBeSaved = createAnimeToBeSaved();

        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        String name = animeSaved.getName();

        Optional<Anime> animeOptional = this.animeRepository.findByName(name);

        Assertions.assertThat(animeOptional).isNotEmpty();

        Assertions.assertThat(animeOptional.get().getId()).isEqualTo(animeSaved.getId());

    }

    @Test
    @DisplayName("Find By Name returns empty list when no anime is found")
    void findByName_ReturnsEmptyList_WhenAnimeIsNotFound(){
        Optional<Anime> animeOptional = this.animeRepository.findByName("xaxa");

        Assertions.assertThat(animeOptional).isEmpty();
    }

    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    void save_ThrowsConstraintViolationException_WhenNameIsEmpty(){
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.animeRepository.save(new Anime()))
                .withMessageContaining("The anime name cannot be empty");
    }
}