package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.requests.AnimeRequestBody;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.AnimeRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.when;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    AnimeController animeController;

    @Mock
    AnimeService animeService;

    @BeforeEach
    void setUp() {
        List<Anime> animeList = List.of(AnimeCreator.createValidAnime());
        PageImpl<Anime> animePage = new PageImpl<>(animeList);

        when(animeService.listAll(any())).thenReturn(animePage);
        when(animeService.listAllNonPageable()).thenReturn(animeList);
        when(animeService.findByIdOrThrowBadRequestException(anyLong())).thenReturn(AnimeCreator.createValidAnime());
        when(animeService.findByNameOrThrowBadRequestException(any())).thenReturn(AnimeCreator.createValidAnime());
        when(animeService.save(any(AnimeRequestBody.class))).thenReturn(AnimeCreator.createValidAnime());
        doNothing().when(animeService).replace(anyLong(), any(AnimeRequestBody.class));
        doNothing().when(animeService).delete(anyLong());
    }

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnListOfAnimesInsidePageObject_WhenSuccessful() {

        String expectedName = AnimeCreator.createValidAnime().getName();

        Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("ListAll returns list of anime when successful")
    void list_ReturnListOfAnimes_WhenSuccessful() {

        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animePage = animeController.listAllNonPageable().getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage).isNotEmpty().hasSize(1);
        Assertions.assertThat(animePage.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful(){
        Long expectedId = AnimeCreator.createValidAnime().getId();

        Anime anime = animeController.findById(1L).getBody();

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findById returns anime when fail")
    void findById_ReturnsAnime_WhenFail(){
        when(animeService.findByIdOrThrowBadRequestException(anyLong()))
                .thenThrow(new BadRequestException("Anime not Found"));

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> animeController.findById(1L))
                .withMessageContaining("Anime not Found");

    }

    @Test
    @DisplayName("findByName returns anime when successful")
    void findByName_ReturnsAnime_WhenSuccessful(){
        Long expectedId = AnimeCreator.createValidAnime().getId();

        Anime anime = animeController.search(AnimeRequestBodyCreator.createAnimeRequestBody()).getBody();

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns anime when fail")
    void findByName_ReturnsAnime_WhenFail(){
        when(animeService.findByNameOrThrowBadRequestException(any()))
                .thenThrow(new BadRequestException("Anime not Found"));

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> animeController.search(AnimeRequestBodyCreator.createAnimeRequestBody()))
                .withMessageContaining("Anime not Found");

    }

    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful(){

        Anime anime = animeController.save(AnimeRequestBodyCreator.createAnimeRequestBody()).getBody();

        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createValidAnime());

    }

    @Test
    @DisplayName("replace updates anime when successful")
    void replace_UpdatesAnime_WhenSuccessful(){

        Assertions.assertThatCode(() -> animeController.replace(1L, AnimeRequestBodyCreator.createAnimeRequestBody()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.replace(1L, AnimeRequestBodyCreator.createAnimeRequestBody());

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){

        Assertions.assertThatCode(() ->animeController.delete(1L))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.delete(1L);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}