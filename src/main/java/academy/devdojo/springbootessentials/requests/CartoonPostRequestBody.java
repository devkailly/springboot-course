package academy.devdojo.springbootessentials.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartoonPostRequestBody {
    @NotEmpty(message = "The cartoon name cannot be empty")
    @Schema(description = "This is the Cartoon's name", example = "Tensei Shittara Slime Datta Ken", required = true)
    private String name;
}
