package com.banyan.stack.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "TV Show entity with comprehensive details from TVMaze API")
public class TvShow {

    @Id
    @Schema(description = "Unique identifier from TVMaze", example = "1")
    private Long id;

    @Schema(description = "TVMaze show URL", example = "https://www.tvmaze.com/shows/1/under-the-dome")
    private String url;

    @Schema(description = "Show name", example = "Breaking Bad")
    private String name;

    @Schema(description = "Show type", example = "Scripted")
    private String type;

    @Schema(description = "Primary language", example = "English")
    private String language;

    @ElementCollection(fetch = FetchType.EAGER)
    @Schema(description = "List of genres", example = "[\"Drama\", \"Crime\", \"Thriller\"]")
    private List<String> genres;

    @Schema(description = "Current status", example = "Ended")
    private String status;

    @Schema(description = "Episode runtime in minutes", example = "47")
    private Integer runtime;

    @Schema(description = "Average episode runtime", example = "47")
    private Integer averageRuntime;

    @Schema(description = "Premiere date", example = "2008-01-20")
    private LocalDate premiered;

    @Schema(description = "End date", example = "2013-09-29")
    private LocalDate ended;

    @Schema(description = "Official website URL")
    private String officialSite;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "time", column = @Column(name = "schedule_time"))
    })
    @Schema(description = "Broadcast schedule information")
    private Schedule schedule;

    @Embedded
    @AttributeOverride(name = "average", column = @Column(name = "rating_average"))
    @Schema(description = "Show rating details")
    private Rating rating;

    @Schema(description = "Show weight/priority", example = "95")
    private Integer weight;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "network_id")),
            @AttributeOverride(name = "name", column = @Column(name = "network_name")),
            @AttributeOverride(name = "officialSite", column = @Column(name = "network_official_site")),
            @AttributeOverride(name = "country.name", column = @Column(name = "network_country_name")),
            @AttributeOverride(name = "country.code", column = @Column(name = "network_country_code")),
            @AttributeOverride(name = "country.timezone", column = @Column(name = "network_country_timezone"))
    })
    @Schema(description = "Broadcasting network information")
    private Network network;

    @Schema(description = "Web channel name")
    private String webChannel;

    @Schema(description = "DVD release country")
    private String dvdCountry;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "tvrage", column = @Column(name = "external_tvrage")),
            @AttributeOverride(name = "thetvdb", column = @Column(name = "external_thetvdb")),
            @AttributeOverride(name = "imdb", column = @Column(name = "external_imdb"))
    })
    @Schema(description = "External database identifiers")
    private Externals externals;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "medium", column = @Column(name = "image_medium")),
            @AttributeOverride(name = "original", column = @Column(name = "image_original"))
    })
    @Schema(description = "Show poster/image URLs")
    private Image image;

    @Lob
    @Schema(description = "Show summary/description")
    private String summary;

    @Schema(description = "Last update timestamp")
    private Long updated;
}
