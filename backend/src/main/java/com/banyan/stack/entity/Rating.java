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
@Schema(description = "TV show rating information")
public class Rating {
    @Schema(description = "Average rating score", example = "8.5")
    private Double average;
}
