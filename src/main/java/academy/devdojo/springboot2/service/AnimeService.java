package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.mapper.AnimeMapper;
import academy.devdojo.springboot2.repository.AnimeRespository;
import academy.devdojo.springboot2.requests.AnimeRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRespository respository;

    public List<Anime> listAll() {
        return respository.findAll();
    }

    public Anime findByIdOrThrowBadRequestException(Long id) {
        return respository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not Found"));
    }

    public Anime findByNameOrThrowBadRequestException(AnimeRequestBody animeRequestBody) {
        return respository.findByName(animeRequestBody.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not Found"));
    }

    public Anime save(AnimeRequestBody animeRequestBody) {
        return respository.save(AnimeMapper.INSTANCE.toAnime(animeRequestBody));

    }

    public void delete(Long id) {
        respository.deleteById(id);
    }

    public void replace(Long id, AnimeRequestBody animeRequestBody) {
        Anime anime = findByIdOrThrowBadRequestException(id);
        anime.setName(animeRequestBody.getName());
        respository.save(anime);
    }
}
