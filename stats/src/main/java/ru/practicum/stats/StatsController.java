package ru.practicum.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.model.ViewStats;
import ru.practicum.stats.model.dto.StatsClientDto;


import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
public class StatsController {


    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/hit")
    public StatsClientDto save(@Valid @RequestBody StatsClientDto statsClientDto) {
        log.info("add ru.practicum.event in statistic");
        return statsService.save(statsClientDto);
    }

    @GetMapping("/stats")
    public List<ViewStats> getViewStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false, defaultValue = "false")
            Boolean unique) {

        log.info("get views");
        return statsService.getViewStats(start, end, uris, unique);
    }
}
