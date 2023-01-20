package ru.practicum.stats.model.dto;

import ru.practicum.stats.model.StatsClient;

public class StatsClientMapper {

    public static StatsClientDto toStatsClientDto(StatsClient statsClient) {
        return StatsClientDto.builder()
                .id(statsClient.getId())
                .app(statsClient.getApp())
                .uri(statsClient.getUri())
                .ip(statsClient.getIp())
                .timestamp(statsClient.getTimestamp())
                .build();
    }

    public static StatsClient toStatsClient(StatsClientDto statsClientDto) {
        return StatsClient.builder()
                .app(statsClientDto.getApp())
                .uri(statsClientDto.getUri())
                .ip(statsClientDto.getIp())
                .timestamp(statsClientDto.getTimestamp())
                .build();
    }
}
