package com.banyan.stack.controller;

import com.banyan.stack.entity.TvShow;
import com.banyan.stack.service.TvShowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://frontend:3000"})
@Tag(name = "TV Shows", description = "TV Show API with TVMaze integration")
public class TvShowController {

    private final TvShowService tvShowService;

    @GetMapping("/shows")
    @Operation(summary = "Get TV Shows with Pagination", description = "Retrieves TV shows with pagination support")
    @ApiResponse(responseCode = "200", description = "Paginated list of TV shows")
    public ResponseEntity<org.springframework.data.domain.Page<TvShow>> getAllShows(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(tvShowService.getAllShows(page, size));
    }

    @GetMapping("/shows/{id}")
    @Operation(summary = "Get TV Show by ID", description = "Retrieves a specific TV show by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TV show found"),
            @ApiResponse(responseCode = "404", description = "TV show not found")
    })
    public ResponseEntity<TvShow> getShowById(
            @Parameter(description = "TV Show ID", example = "1")
            @PathVariable Long id) {
        return tvShowService.getShowById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/shows")
    @Operation(summary = "Search TV Shows", description = "Search TV shows by name in local database")
    @ApiResponse(responseCode = "200", description = "Search results")
    public ResponseEntity<List<TvShow>> searchShows(
            @Parameter(description = "Search query", example = "breaking bad")
            @RequestParam String q) {
        return ResponseEntity.ok(tvShowService.searchShows(q));
    }

    // TVMaze API Operations
    @GetMapping("/tvmaze/search")
    @Operation(summary = "Search Shows from TVMaze API", description = "Search TV shows directly from TVMaze API")
    @ApiResponse(responseCode = "200", description = "TVMaze search results")
    public ResponseEntity<List<com.banyan.stack.dto.TVMazeSearchResponse>> searchShowsFromAPI(
            @Parameter(description = "Search query", example = "breaking bad")
            @RequestParam String q) {
        return ResponseEntity.ok(tvShowService.searchShowsFromAPI(q));
    }

    @GetMapping("/tvmaze/shows/{id}")
    @Operation(summary = "Get Show from TVMaze API", description = "Get TV show details directly from TVMaze API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TVMaze show details"),
            @ApiResponse(responseCode = "404", description = "Show not found on TVMaze")
    })
    public ResponseEntity<com.banyan.stack.dto.TVMazeShowResponse> getShowFromAPI(
            @Parameter(description = "TVMaze Show ID", example = "1")
            @PathVariable Long id) {
        com.banyan.stack.dto.TVMazeShowResponse show = tvShowService.getShowFromAPI(id);
        return show != null ? ResponseEntity.ok(show) : ResponseEntity.notFound().build();
    }
}