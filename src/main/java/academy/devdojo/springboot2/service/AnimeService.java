package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.mapper.AnimeMapper;
import academy.devdojo.springboot2.repository.AnimeRespository;
import academy.devdojo.springboot2.requests.AnimeRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRespository respository;

    public Page<Anime> listAll(Pageable pageable) {
        return respository.findAll(pageable);
    }
    public List<Anime> listAllNonPageable() {
        return respository.findAll();
    }

    public Anime findByIdOrThrowBadRequestException(Long id) {
        return respository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime not Found"));
    }

    public Anime findByNameOrThrowBadRequestException(AnimeRequestBody animeRequestBody) {
        return respository.findByName(animeRequestBody.getName())
                .orElseThrow(() -> new BadRequestException("Anime not Found"));
    }

    @Transactional
    public Anime save(AnimeRequestBody animeRequestBody) {
        return respository.save(AnimeMapper.INSTANCE.toAnime(animeRequestBody));

    }

    @Transactional
    public void delete(Long id) {
        respository.deleteById(id);
    }

    @Transactional
    public void replace(Long id, AnimeRequestBody animeRequestBody) {
        Anime anime = findByIdOrThrowBadRequestException(id);
        anime.setName(animeRequestBody.getName());
        respository.save(anime);
    }
}
