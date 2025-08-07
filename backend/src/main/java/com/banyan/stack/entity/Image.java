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
@Schema(description = "Show poster/image URLs")
public class Image {
    @Schema(description = "Medium size image URL")
    private String medium;

    @Schema(description = "Original size image URL")
    private String original;
}
