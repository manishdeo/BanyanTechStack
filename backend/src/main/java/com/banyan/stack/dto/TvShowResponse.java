package com.banyan.stack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "TV Show response DTO")
public class TvShowResponse {
    
    @Schema(description = "Show ID", example = "1")
    private Long id;
    
    @Schema(description = "Show name", example = "Breaking Bad")
    private String name;
    
    @Schema(description = "Show type", example = "Scripted")
    private String type;
    
    @Schema(description = "Primary language", example = "English")
    private String language;
    
    @Schema(description = "List of genres")
    private List<String> genres;
    
    @Schema(description = "Current status", example = "Ended")
    private String status;
    
    @Schema(description = "Episode runtime in minutes", example = "47")
    private Integer runtime;
    
    @Schema(description = "Premiere date")
    private LocalDate premiered;
    
    @Schema(description = "End date")
    private LocalDate ended;
    
    @Schema(description = "Average rating", example = "8.5")
    private Double rating;
    
    @Schema(description = "Network name", example = "AMC")
    private String network;
    
    @Schema(description = "Show summary")
    private String summary;
    
    @Schema(description = "Medium image URL")
    private String imageUrl;
}