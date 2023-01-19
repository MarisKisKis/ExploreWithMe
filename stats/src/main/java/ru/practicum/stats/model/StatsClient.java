package ru.practicum.stats.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stats")
public class StatsClient {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "app", length = 120, nullable = false)
    private String app;

    @NotBlank
    @Column(name = "uri", length = 8000, nullable = false)
    private String uri;

    @NotBlank
    @Column(name = "ip", length = 120, nullable = false)
    private String ip;

    @NotBlank
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}
