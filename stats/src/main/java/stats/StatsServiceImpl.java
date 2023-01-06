package stats;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stats.model.StatsClient;
import stats.model.ViewStats;
import stats.model.dto.StatsClientDto;
import stats.model.dto.StatsClientMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {

    @Autowired
    private StatsRepository statsRepository;

    @Override
    public StatsClientDto save(StatsClientDto statsClientDto) {

        StatsClient endPointStatsClient = StatsClientMapper.toStatsClient(statsClientDto);
        endPointStatsClient.setTimestamp(LocalDateTime.now());
        return StatsClientMapper.toStatsClientDto(statsRepository.save(endPointStatsClient));
    }

    @Override
    public List<ViewStats> getViewStats(String start, String end, List<String> uris, Boolean unique) {

        LocalDateTime startTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endTime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (unique) {
            return statsRepository.findAllUnique(startTime, endTime, uris, unique);
        } else {
            return statsRepository.findAll(startTime, endTime, uris);
        }
    }
}
