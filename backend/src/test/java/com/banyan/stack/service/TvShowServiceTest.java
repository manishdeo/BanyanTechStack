package com.banyan.stack.service;

import com.banyan.stack.config.TestConfig;
import com.banyan.stack.entity.*;
import com.banyan.stack.repository.TvShowRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {com.banyan.stack.StackApplication.class, TestConfig.class})
@ActiveProfiles("test")
class TvShowServiceTest {

    @Autowired
    private TvShowRepository repository;

    @Test
    void testTvShowEntityPersistence() {
        // Create test data
        TvShow show = TvShow.builder()
                .id(1L)
                .name("Test Show")
                .type("Scripted")
                .language("English")
                .genres(List.of("Drama", "Comedy"))
                .status("Running")
                .runtime(30)
                .premiered(LocalDate.of(2020, 1, 1))
                .schedule(new Schedule("20:00", List.of("Monday", "Tuesday")))
                .rating(new Rating(8.5))
                .network(new Network(1L, "Test Network",
                        new Country("United States", "US", "America/New_York"),
                        "https://test.com"))
                .externals(new Externals(123, 456, "tt1234567"))
                .image(new Image("medium.jpg", "original.jpg"))
                .summary("Test summary")
                .build();

        // Save and retrieve
        repository.save(show);
        TvShow retrieved = repository.findById(1L).orElse(null);

        // Verify
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getName()).isEqualTo("Test Show");
        assertThat(retrieved.getGenres()).containsExactly("Drama", "Comedy");
        assertThat(retrieved.getSchedule().getTime()).isEqualTo("20:00");
        assertThat(retrieved.getSchedule().getDays()).containsExactly("Monday", "Tuesday");
        assertThat(retrieved.getRating().getAverage()).isEqualTo(8.5);
        assertThat(retrieved.getNetwork().getName()).isEqualTo("Test Network");
        assertThat(retrieved.getNetwork().getCountry().getName()).isEqualTo("United States");
        assertThat(retrieved.getExternals().getImdb()).isEqualTo("tt1234567");
        assertThat(retrieved.getImage().getMedium()).isEqualTo("medium.jpg");
    }
}