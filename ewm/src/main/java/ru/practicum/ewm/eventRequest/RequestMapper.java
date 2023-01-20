package ru.practicum.ewm.eventRequest;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.User;

public class RequestMapper {

    public static RequestDto toEventRequestDto(EventRequest eventRequest) {
        return RequestDto.builder()
                .id(eventRequest.getId())
                .requester(eventRequest.getUser().getId())
                .event(eventRequest.getEvent().getId())
                .status(eventRequest.getStatus())
                .created(eventRequest.getCreated())
                .build();
    }

    public static EventRequest toEventRequest(RequestDto eventRequestDto, User user, Event event) {
        return EventRequest.builder()
                .id(eventRequestDto.getId())
                .user(user)
                .event(event)
                .status(eventRequestDto.getStatus())
                .build();
    }
}
