package client;

import event.model.Event;

import java.util.List;
import java.util.Map;

public interface ClientService {

    Map<Long, Long> getViewsForEvents(List<Event> events, Boolean unique);

    Long getViewsForEvent(Event event, Boolean unique);
}
