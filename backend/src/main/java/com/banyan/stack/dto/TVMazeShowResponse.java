package com.banyan.stack.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TVMazeShowResponse {
    private Long id;
    private String url;
    private String name;
    private String type;
    private String language;
    private List<String> genres;
    private String status;
    private Integer runtime;
    private Integer averageRuntime;
    private String premiered;
    private String ended;
    private String officialSite;
    private TVMazeSchedule schedule;
    private TVMazeRating rating;
    private Integer weight;
    private TVMazeNetwork network;
    private Object webChannel;
    private String dvdCountry;
    private TVMazeExternals externals;
    private TVMazeImage image;
    private String summary;
    private Long updated;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TVMazeSchedule {
        private String time;
        private List<String> days;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TVMazeRating {
        private Double average;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TVMazeNetwork {
        private Long id;
        private String name;
        private TVMazeCountry country;
        private String officialSite;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TVMazeCountry {
        private String name;
        private String code;
        private String timezone;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TVMazeExternals {
        private Integer tvrage;
        private Integer thetvdb;
        private String imdb;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TVMazeImage {
        private String medium;
        private String original;
    }
}