package com.banyan.stack.controller;

import com.banyan.stack.service.TvShowService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Data Loading", description = "TV Show data loading operations")
public class LoadController {

    private final TvShowService tvShowService;

    @PostMapping("/shows/load")
    @Operation(summary = "Load TV Shows", description = "Load TV shows from tvtitles.txt file. First 100 loaded synchronously, rest processed in background.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Shows loading initiated")
    public ResponseEntity<LoadResponse> loadShows() {
        int count = tvShowService.loadTVTitlesFromFile();
        return ResponseEntity.ok(new LoadResponse(
                "Loading initiated successfully",
                count,
                "First " + count + " shows loaded immediately, remaining processed in background"
        ));
    }

    public static class LoadResponse {
        public final String status;
        public final int initialCount;
        public final String message;

        public LoadResponse(String status, int initialCount, String message) {
            this.status = status;
            this.initialCount = initialCount;
            this.message = message;
        }
    }
}