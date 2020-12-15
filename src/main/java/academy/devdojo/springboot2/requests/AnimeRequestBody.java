package academy.devdojo.springboot2.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimeRequestBody {

    @NotEmpty(message = "The anime cannot be empty")
    @Schema(description = "This is the Anime's name", example = "Tensei Shittara Sleme Datta Ken", required = true)
    private String name;
}
