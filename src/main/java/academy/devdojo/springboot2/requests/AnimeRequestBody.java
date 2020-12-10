package academy.devdojo.springboot2.requests;

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
    private String name;
}
