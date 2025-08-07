package com.banyan.stack.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "TV show broadcast schedule")
public class Schedule {
    @Schema(description = "Broadcast time", example = "20:00")
    private String time;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tv_show_schedule_days", joinColumns = @JoinColumn(name = "tv_show_id"))
    @Column(name = "schedule_day")
    @Schema(description = "Broadcast days", example = "[\"Monday\", \"Tuesday\"]")
    private List<String> days;
}
