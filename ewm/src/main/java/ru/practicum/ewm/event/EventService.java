package ru.practicum.ewm.event;


import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventDtoInfo;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.model.EventStatus;
import ru.practicum.ewm.event.model.Sort;
import ru.practicum.ewm.eventRequest.RequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventFullDto> getEvents(List<Long> userIds, List<EventStatus> stateIds, List<Integer> catIds,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateEvent(EventDtoInfo eventDtoInfo, Long eventId);

    EventFullDto publishEvent(Long eventId);

    EventFullDto rejectEvent(Long eventId);

    List<EventDto> getPublicEvents(String text, List<Integer> catIds, Boolean paid, LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd, Boolean onlyAvailable, Sort sort, Integer from, Integer size);

    EventFullDto getPublicEventById(Long eventId);

    List<EventDto> getEventsByCreator(Long userId, Integer from, Integer size);

    EventFullDto updateEventByCreator(Long userId, EventDtoInfo eventDtoInfo);

    EventFullDto createEvent(Long userId, EventDtoInfo eventDtoInfo);

    EventFullDto getEventByIdForCreator(Long userId, Long eventId);

    EventFullDto cancelEventByCreator(Long userId, Long eventId);

    List<RequestDto> getRequestEventByUser(Long userId, Long eventId);

    RequestDto confirmRequestForEvent(Long userId, Long eventId, Long reqId);

    RequestDto rejectRequestForEvent(Long userId, Long eventId, Long reqId);

}
