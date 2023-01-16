package ru.practicum.stats;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats.model.StatsClient;
import ru.practicum.stats.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<StatsClient, Long> {

    @Query("SELECT new ru.practicum.stats.model.ViewStats(e.app, e.uri, COUNT (e.ip)) " +
            "from StatsClient e WHERE e.timestamp> ?1 AND e.timestamp< ?2 and e.uri in ?3 GROUP BY e.app, e.uri")
    List<ViewStats> findAll(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "SELECT new ru.practicum.stats.model.ViewStats(e.app, e.uri, COUNT (DISTINCT e.ip)) from " +
            "StatsClient e WHERE e.timestamp> ?1 AND e.timestamp< ?2 and e.uri in ?3 GROUP BY e.app, e.uri")
    List<ViewStats> findAllUnique(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
