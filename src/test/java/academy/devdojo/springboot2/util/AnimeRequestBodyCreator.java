package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.requests.AnimeRequestBody;

public class AnimeRequestBodyCreator {

    public static AnimeRequestBody createAnimeRequestBody() {
        return AnimeRequestBody.builder()
                .name(AnimeCreator.createAnimeToBeSaved().getName())
                .build();
    }
}
