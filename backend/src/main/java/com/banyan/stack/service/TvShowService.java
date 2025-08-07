package com.banyan.stack.service;

import com.banyan.stack.dto.TVMazeSearchResponse;
import com.banyan.stack.dto.TVMazeShowResponse;
import com.banyan.stack.entity.*;
import com.banyan.stack.repository.TvShowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class TvShowService {

    private static final String TVMAZE_BASE_URL = "https://api.tvmaze.com";
    private final TvShowRepository tvShowRepository;
    private final WebClient webClient;

    public int loadTVTitlesFromFile() {
        try (InputStream inputStream = getClass().getResourceAsStream("/tvtitles.txt")) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                List<String> titles = reader.lines().toList();
                int totalTitles = titles.size();

                // Load first 100 synchronously
                List<String> firstBatch = titles.subList(0, Math.min(100, totalTitles));
                List<TvShow> initialShows = new java.util.ArrayList<>();

                for (String title : firstBatch) {
                    TvShow show = fetchShow(title);
                    if (show != null) {
                        initialShows.add(show);
                    }
                    Thread.sleep(100);
                }

                tvShowRepository.saveAll(initialShows);
                log.info("Loaded initial {} shows, processing remaining {} in background", initialShows.size(), totalTitles - firstBatch.size());

                // Process remaining in parallel
                if (totalTitles > 100) {
                    List<String> remainingTitles = titles.subList(100, totalTitles);
                    CompletableFuture.runAsync(() -> loadRemainingTitles(remainingTitles));
                }

                return initialShows.size();

            }
        } catch (Exception e) {
            log.error("Error loading TV titles from file", e);
            return 0;
        }
    }

    private void loadRemainingTitles(List<String> titles) {
        titles.parallelStream().forEach(title -> {
            try {
                TvShow show = fetchShow(title);
                if (show != null) {
                    tvShowRepository.save(show);
                }
                Thread.sleep(100);
            } catch (Exception e) {
                log.warn("Failed to load show: {}", title);
            }
        });
        log.info("Background loading completed for {} titles", titles.size());
    }

    private TvShow fetchShow(String title) {
        try {
            TVMazeShowResponse response = webClient
                    .get()
                    .uri(TVMAZE_BASE_URL + "/singlesearch/shows?q={title}", title)
                    .retrieve()
                    .bodyToMono(TVMazeShowResponse.class)
                    .block();

            if (response != null) {
                return convertToEntity(response);
            }
        } catch (Exception e) {
            log.warn("Failed to fetch show: {}", title);
        }
        return null;
    }

    public org.springframework.data.domain.Page<TvShow> getAllShows(int page, int size) {
        return tvShowRepository.findAll(org.springframework.data.domain.PageRequest.of(page, size));
    }

    public List<TvShow> getAllShows() {
        return tvShowRepository.findAll();
    }

    @Cacheable(value = "tvshows", key = "#id")
    public Optional<TvShow> getShowById(Long id) {
        return tvShowRepository.findById(id);
    }

    @Cacheable(value = "search-results", key = "#query")
    public List<TvShow> searchShows(String query) {
        return tvShowRepository.findByNameContainingIgnoreCase(query);
    }

    // TVMaze API Operations
    public List<TVMazeSearchResponse> searchShowsFromAPI(String query) {
        try {
            return webClient
                    .get()
                    .uri(TVMAZE_BASE_URL + "/search/shows?q={query}", query)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<TVMazeSearchResponse>>() {
                    })
                    .block();
        } catch (Exception e) {
            log.error("Failed to search shows from TVMaze API: {}", query, e);
            return List.of();
        }
    }

    public TVMazeShowResponse getShowFromAPI(Long id) {
        try {
            return webClient
                    .get()
                    .uri(TVMAZE_BASE_URL + "/shows/{id}", id)
                    .retrieve()
                    .bodyToMono(TVMazeShowResponse.class)
                    .block();
        } catch (Exception e) {
            log.error("Failed to get show from TVMaze API: {}", id, e);
            return null;
        }
    }


    private TvShow convertToEntity(TVMazeShowResponse response) {
        return TvShow.builder()
                .id(response.getId())
                .url(response.getUrl())
                .name(response.getName())
                .type(response.getType())
                .language(response.getLanguage())
                .genres(response.getGenres())
                .status(response.getStatus())
                .runtime(response.getRuntime())
                .averageRuntime(response.getAverageRuntime())
                .premiered(parseDate(response.getPremiered()))
                .ended(parseDate(response.getEnded()))
                .officialSite(response.getOfficialSite())
                .schedule(convertSchedule(response.getSchedule()))
                .rating(convertRating(response.getRating()))
                .weight(response.getWeight())
                .network(convertNetwork(response.getNetwork()))
                .webChannel(convertWebChannel(response.getWebChannel()))
                .dvdCountry(response.getDvdCountry())
                .externals(convertExternals(response.getExternals()))
                .image(convertImage(response.getImage()))
                .summary(response.getSummary())
                .updated(response.getUpdated())
                .build();
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            return null;
        }
    }

    private Schedule convertSchedule(TVMazeShowResponse.TVMazeSchedule schedule) {
        if (schedule == null) return null;
        return new Schedule(schedule.getTime(), schedule.getDays());
    }

    private Rating convertRating(TVMazeShowResponse.TVMazeRating rating) {
        if (rating == null) return null;
        return new Rating(rating.getAverage());
    }

    private Network convertNetwork(TVMazeShowResponse.TVMazeNetwork network) {
        if (network == null) return null;
        return new Network(network.getId(), network.getName(),
                convertCountry(network.getCountry()), network.getOfficialSite());
    }

    private Country convertCountry(TVMazeShowResponse.TVMazeCountry country) {
        if (country == null) return null;
        return new Country(country.getName(), country.getCode(), country.getTimezone());
    }

    private Externals convertExternals(TVMazeShowResponse.TVMazeExternals externals) {
        if (externals == null) return null;
        return new Externals(externals.getTvrage(), externals.getThetvdb(), externals.getImdb());
    }

    private Image convertImage(TVMazeShowResponse.TVMazeImage image) {
        if (image == null) return null;
        return new Image(image.getMedium(), image.getOriginal());
    }

    private String convertWebChannel(Object webChannel) {
        if (webChannel == null) return null;
        if (webChannel instanceof String) return (String) webChannel;
        return webChannel.toString();
    }
}