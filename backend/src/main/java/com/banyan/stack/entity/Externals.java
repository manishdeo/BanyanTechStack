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
@Schema(description = "External database identifiers")
public class Externals {
    @Schema(description = "TVRage ID", example = "24493")
    private Integer tvrage;

    @Schema(description = "TheTVDB ID", example = "81189")
    private Integer thetvdb;

    @Schema(description = "IMDB ID", example = "tt0903747")
    private String imdb;
}
