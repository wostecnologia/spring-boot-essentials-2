package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.LinkedHashMap;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AnimeControllerTestIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("list returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("ListAll returns list of anime when successful")
    void list_ReturnListOfAnimes_WhenSuccessful() {

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        List<Anime> animePage = testRestTemplate.exchange("/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
        }).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage).isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("findById returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful(){

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        Long expectedId = AnimeCreator.createValidAnime().getId();

        Anime anime = testRestTemplate.getForObject("/animes/{id}", Anime.class, expectedId);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findById returns anime when fail")
    void findById_ReturnsAnime_WhenFail(){

        LinkedHashMap<String, Object> badRequestException = testRestTemplate.getForObject("/animes/{id}", LinkedHashMap.class, 1L);

        Assertions.assertThat(badRequestException.get("title")).isEqualTo("Bad Request Exception, Check the Documentation");
        Assertions.assertThat(badRequestException.get("status")).isEqualTo(400);
        Assertions.assertThat(badRequestException.get("details")).isEqualTo("Anime not Found");
        Assertions.assertThat(badRequestException.get("developerMessage")).isEqualTo("academy.devdojo.springboot2.exception.BadRequestException");
    }

    @Test
    @DisplayName("findByName returns anime when successful")
    void findByName_ReturnsAnime_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String name = AnimeCreator.createValidAnime().getName();
        Anime anime = testRestTemplate.getForObject("/animes/search?name={name}", Anime.class, name);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getName()).isNotNull().isEqualTo(name);
    }

    @Test
    @DisplayName("findByName returns anime when fail")
    void findByName_ReturnsAnime_WhenFail(){
        String name = AnimeCreator.createValidAnime().getName();
        LinkedHashMap<String, Object> badRequestException = testRestTemplate.getForObject("/animes/search?name={name}", LinkedHashMap.class, name);

        Assertions.assertThat(badRequestException.get("title")).isEqualTo("Bad Request Exception, Check the Documentation");
        Assertions.assertThat(badRequestException.get("status")).isEqualTo(400);
        Assertions.assertThat(badRequestException.get("details")).isEqualTo("Anime not Found");
        Assertions.assertThat(badRequestException.get("developerMessage")).isEqualTo("academy.devdojo.springboot2.exception.BadRequestException");

    }

    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful(){

        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity("/animes", AnimeCreator.createAnimeToBeSaved(), Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();

    }

    @Test
    @DisplayName("replace updates anime when successful")
    void replace_UpdatesAnime_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        savedAnime.setName("new name");

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/{id}",
                HttpMethod.PUT,new HttpEntity<>(savedAnime), Void.class, savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();

        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/{id}",
                HttpMethod.DELETE,null, Void.class, savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();

        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
