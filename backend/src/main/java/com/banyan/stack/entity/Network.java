package com.banyan.stack.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Broadcasting network details")
public class Network {
    @Schema(description = "Network ID", example = "1")
    private Long id;

    @Schema(description = "Network name", example = "AMC")
    private String name;

    @Embedded
    @Schema(description = "Network country information")
    private Country country;

    @Schema(description = "Network official website")
    private String officialSite;
}
