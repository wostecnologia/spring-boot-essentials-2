package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.AnimeRequestBody;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    AnimeService animeService;

    @Mock
    AnimeRepository animeRepository;

    @BeforeEach
    void setUp() {
        List<Anime> animeList = List.of(AnimeCreator.createValidAnime());
        PageImpl<Anime> animePage = new PageImpl<>(animeList);

        when(animeRepository.findAll(any(PageRequest.class))).thenReturn(animePage);
        when(animeRepository.findAll()).thenReturn(animeList);
        when(animeRepository.findById(anyLong())).thenReturn(Optional.of(AnimeCreator.createValidAnime()));
        when(animeRepository.findByName(any())).thenReturn(Optional.of(AnimeCreator.createValidAnime()));
        when(animeRepository.save(any(Anime.class))).thenReturn(AnimeCreator.createValidAnime());
        doNothing().when(animeRepository).deleteById(anyLong());
    }

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnListOfAnimesInsidePageObject_WhenSuccessful() {

        String expectedName = AnimeCreator.createValidAnime().getName();

        Page<Anime> animePage = animeService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAllNonPageable returns list of anime when successful")
    void list_ReturnListOfAnimes_WhenSuccessful() {

        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animePage = animeService.listAllNonPageable();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage).isNotEmpty().hasSize(1);
        Assertions.assertThat(animePage.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns anime when successful")
    void findByIdOrThrowBadRequestException_ReturnsAnime_WhenSuccessful(){
        Long expectedId = AnimeCreator.createValidAnime().getId();

        Anime anime = animeService.findByIdOrThrowBadRequestException(1L);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns anime when fail")
    void findByIdOrThrowBadRequestException_ReturnsAnime_WhenFail(){
        when(animeService.findByIdOrThrowBadRequestException(anyLong()))
                .thenThrow(new BadRequestException("Anime not Found"));

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> animeService.findByIdOrThrowBadRequestException(1L))
                .withMessageContaining("Anime not Found");

    }

    @Test
    @DisplayName("findByNameOrThrowBadRequestException returns anime when successful")
    void findByNameOrThrowBadRequestException_ReturnsAnime_WhenSuccessful(){
        Long expectedId = AnimeCreator.createValidAnime().getId();

        Anime anime = animeService
                .findByNameOrThrowBadRequestException(AnimeRequestBodyCreator.createAnimeRequestBody());

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByNameOrThrowBadRequestException returns anime when fail")
    void findByNameOrThrowBadRequestException_ReturnsAnime_WhenFail(){
        when(animeService.findByNameOrThrowBadRequestException(AnimeRequestBodyCreator.createAnimeRequestBody()))
                .thenThrow(new BadRequestException("Anime not Found"));

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> animeService.findByNameOrThrowBadRequestException(AnimeRequestBodyCreator.createAnimeRequestBody()))
                .withMessageContaining("Anime not Found");

    }

    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful(){

        Anime anime = animeService.save(AnimeRequestBodyCreator.createAnimeRequestBody());

        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createValidAnime());

    }

    @Test
    @DisplayName("replace updates anime when successful")
    void replace_UpdatesAnime_WhenSuccessful(){

        Assertions.assertThatCode(() -> animeService.replace(1L, AnimeRequestBodyCreator.createAnimeRequestBody()))
                .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){

        Assertions.assertThatCode(() -> animeService.delete(1L))
                .doesNotThrowAnyException();
    }
}