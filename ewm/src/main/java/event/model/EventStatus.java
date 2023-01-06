package event.model;

import java.util.Optional;

public enum EventStatus {

    PUBLISHED,
    CANCELED,
    PENDING;

    public static Optional<EventStatus> from(String stringEventState) {
        for (EventStatus eventStatus : values()) {
            if (eventStatus.name().equalsIgnoreCase(stringEventState)) {
                return Optional.of(eventStatus);
            }
        }
        return Optional.empty();
    }
}
