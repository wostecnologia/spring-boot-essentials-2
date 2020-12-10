package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.domain.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved() {
        return Anime.builder()
                .name("Hajume no Ippo")
                .build();
    }


    public static Anime createValidAnime() {
        return Anime.builder()
                .id(1L)
                .name("Hajume no Ippo")
                .build();
    }


    public static Anime createValidUpdateAnime() {
        return Anime.builder()
                .id(1L)
                .name("Hajume no Ippo 2")
                .build();
    }
}
