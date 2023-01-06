package client;


import event.model.Event;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    @Autowired
    private final StatsClient statsClient;

    private static String URI_EVENT = "/events/{%d}";

    public Map<Long, Long> getViewsForEvents(List<Event> events, Boolean unique) {

        Optional<Event> event = events.stream()
                .min(Comparator.comparing(Event::getEventDate));

        if (event.isEmpty()) {
            return new HashMap<>();
        }

        String start = event.get().getCreatedOn().withNano(0).toString();
        String end = LocalDateTime.now().withNano(0).toString();
        List<String> uris = events.stream()
                .map(e -> String.format(URI_EVENT, e.getId()))
                .collect(Collectors.toList());
        List<ViewStats> viewStats = statsClient.getViews(start, end, uris, unique);
        Map<Long, Long> eventViews = new HashMap<>();
        viewStats.forEach(e -> eventViews
                .put(getIdFromUri(e.getUri()), e.getHits()));

        return eventViews;
    }

    @Override
    public Long getViewsForEvent(Event event, Boolean unique) {

        String start = event.getCreatedOn().withNano(0).toString();
        String end = LocalDateTime.now().withNano(0).toString();
        List<String> uris = List.of(String.format(URI_EVENT, event.getId()));
        List<ViewStats> views = statsClient.getViews(start, end, uris, unique);

        if (views.isEmpty()) {
            return 0L;
        }
        return views.get(0).getHits();
    }

    private Long getIdFromUri(String uri) {
        return Long.parseLong(StringUtils.getDigits(uri));
    }
}
