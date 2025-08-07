package com.banyan.stack.config;

import com.banyan.stack.service.TvShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.data.initialize", havingValue = "true", matchIfMissing = true)
public class DataInitializer implements ApplicationRunner {

    private final TvShowService tvShowService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting TV shows data initialization...");
        int loadedCount = tvShowService.loadTVTitlesFromFile();
        log.info("Data initialization completed. Loaded {} TV shows", loadedCount);
    }
}