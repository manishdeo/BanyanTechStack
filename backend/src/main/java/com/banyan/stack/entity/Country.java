package com.banyan.stack.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Country information")
public class Country {
    @Schema(description = "Country name", example = "United States")
    private String name;

    @Schema(description = "Country code", example = "US")
    private String code;

    @Schema(description = "Timezone", example = "America/New_York")
    private String timezone;
}
