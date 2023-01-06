package stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import stats.model.ViewStats;
import stats.model.dto.StatsClientDto;


import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class StatsController {

    @Autowired
    private final StatsService statsService;

    @PostMapping("/hit")
    public StatsClientDto save(@Valid @RequestBody StatsClientDto endPointStatsClientDto) {
        log.info("add event in statistic");
        return statsService.save(endPointStatsClientDto);
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
