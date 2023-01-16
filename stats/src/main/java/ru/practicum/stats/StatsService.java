package ru.practicum.stats;

import ru.practicum.stats.model.ViewStats;
import ru.practicum.stats.model.dto.StatsClientDto;

import java.util.List;

public interface StatsService {

    StatsClientDto save(StatsClientDto statsClientDto);

    List<ViewStats> getViewStats(String startDate, String endDate, List<String> uriIds, Boolean unique);
}
