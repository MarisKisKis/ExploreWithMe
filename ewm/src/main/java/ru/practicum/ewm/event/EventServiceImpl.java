package ru.practicum.ewm.event;


import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.client.ClientService;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventDtoInfo;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventStatus;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.Sort;
import ru.practicum.ewm.eventRequest.*;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    @Autowired
    private final EventRepository eventRepository;

    @Autowired
    private final RequestRepository eventRequestRepository;

    @Autowired
    private final ClientService statClientService;

    @Override
    public List<EventFullDto> getEvents(List<Long> userIds, List<EventStatus> stateIds, List<Integer> catIds,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size);
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }

        List<Event> events = eventRepository.searchEvents(userIds, stateIds, catIds, rangeStart, rangeEnd, pageable);
        return toListEventFullDto(events, false);
    }

    @Override
    public EventFullDto updateEvent(EventDtoInfo eventDtoInfo, Long eventId) {

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with ID %s not found", eventId)));
        checkChanges(eventDtoInfo, event);

        if (eventDtoInfo.getRequestModeration() != null) {
            event.setRequestModeration(eventDtoInfo.getRequestModeration());
        }

        if (eventDtoInfo.getLocation() != null) {
            Location location = Location.builder()
                    .lon(eventDtoInfo.getLocation().getLon())
                    .lat(eventDtoInfo.getLocation().getLat())
                    .build();
            event.setLocation(location);
        }

        return makeFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto publishEvent(Long eventId) {

        Event event = getEventById(eventId);

        if (LocalDateTime.now().plusHours(1).isAfter(event.getEventDate())) {
            throw new ValidationException("Not enough time before ru.practicum.event");
        }

        if (!event.getState().equals(EventStatus.PENDING)) {
            throw new ValidationException("Event has to be in PENDING status");
        }

        event.setState(EventStatus.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());

        return makeFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto rejectEvent(Long eventId) {

        Event event = getEventById(eventId);
        if (event.getState().equals(EventStatus.PUBLISHED)) {
            throw new ValidationException("You are ADMIN, but you don't have power in this case");
        }
        event.setState(EventStatus.CANCELED);

        return makeFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventDto> getPublicEvents(String text, List<Integer> catIds, Boolean paid, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, Boolean onlyAvailable, Sort sort, Integer from, Integer size) {

        if (Objects.isNull(rangeStart)) {
            rangeStart = LocalDateTime.now();
        }

        List<Event> events = new ArrayList<>(eventRepository
                .findEvents(text, catIds, paid, rangeStart, rangeEnd, onlyAvailable));

        if (sort != null) {
            switch (sort) {
                case EVENT_DATE:
                    events = events.stream()
                            .sorted(Comparator.comparing(Event::getEventDate))
                            .collect(Collectors.toList());
                    break;
                case VIEWS:
                    List<EventDto> eventDto = toListEventDto(events, false);
                    return eventDto.stream()
                            .sorted(Comparator.comparingLong(EventDto::getViews).reversed())
                            .collect(Collectors.toList());
            }
        }

        return events.stream()
                .map(EventMapper::toEventDto)
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getPublicEventById(Long eventId) {

        Event event = eventRepository.findByIdAndState(eventId, EventStatus.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(String.format("Event with ID = %s wasn't found", eventId)));

        return makeFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventDto> getEventsByCreator(Long userId, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(
                from / size,
                size);
        return toListEventDto(eventRepository.findAllByInitiatorId(userId, pageable), false);
    }

    @Override
    public EventFullDto updateEventByCreator(Long userId, EventDtoInfo eventDtoInfo) {

        Event event = eventRepository.findById(eventDtoInfo.getEventId())
                .orElseThrow(() -> new NotFoundException("Not found ru.practicum.event with id = " + eventDtoInfo.getEventId()));

        if (LocalDateTime.now().plusHours(2).isAfter(eventDtoInfo.getEventDate())) {
            throw new ValidationException(String.format("wrong time %s", eventDtoInfo.getEventDate().toString()));
        }
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Only initiator or admin can edit ru.practicum.event");
        }
        if (event.getState().equals(EventStatus.PUBLISHED)) {
            throw new ValidationException("We can't edit a published ru.practicum.event");
        }
        if (event.getState().equals(EventStatus.CANCELED)) {
            event.setState(EventStatus.PENDING);
        }

        return updateEvent(eventDtoInfo, event.getId());
    }

    @Override
    public EventFullDto createEvent(Long userId, EventDtoInfo eventDtoInfo) {

        if (LocalDateTime.now().plusHours(2).isAfter(eventDtoInfo.getEventDate())) {
            throw new ValidationException(String.format("wrong time %s", eventDtoInfo.getEventDate().toString()));
        }
        Event event = EventMapper.toEvent(userId, eventDtoInfo);
        eventRepository.save(event);

        return makeFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEventByIdForCreator(Long userId, Long eventId) {
        return makeFullDto(eventRepository.findByIdAndInitiatorId(eventId, userId));
    }

    @Override
    public EventFullDto cancelEventByCreator(Long userId, Long eventId) {

        getEventById(eventId);
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        event.setState(EventStatus.CANCELED);

        return makeFullDto(eventRepository.save(event));
    }

    @Override
    public List<RequestDto> getRequestEventByUser(Long userId, Long eventId) {

        Event event = getEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Only initiator can get this information");
        }

        return eventRequestRepository.findAllByEventId(eventId)
                .stream()
                .map(RequestMapper::toEventRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto confirmRequestForEvent(Long userId, Long eventId, Long reqId) {

        Event event = getEventById(eventId);
        EventRequest currentEventRequest = eventRequestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException(String.format("EventRequest with ID = %s wasn't found", reqId)));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Only initiator can confirm a request");
        }
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ValidationException("No need to confirm");
        }
        if (isRequestLimitReached(event)) {
            throw new ValidationException("No more requests available");
        }
        currentEventRequest.setStatus(RequestStatus.CONFIRMED);
        if (isRequestLimitReached(event)) {
            event.getRequests().stream()
                    .filter(e -> !e.getStatus().equals(RequestStatus.CONFIRMED))
                    .forEach(eventRequest -> eventRequest.setStatus(RequestStatus.CANCELED));
        }
        eventRepository.save(event);

        return RequestMapper.toEventRequestDto(currentEventRequest);
    }

    @Override
    public RequestDto rejectRequestForEvent(Long userId, Long eventId, Long reqId) {

        Event event = getEventById(eventId);
        EventRequest currentEventRequest = eventRequestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException(String.format("EventRequest with ID = %s wasn't found", reqId)));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Only initiator can reject a request");
        }

        event.getRequests().remove(currentEventRequest);
        currentEventRequest.setStatus(RequestStatus.REJECTED);
        eventRepository.save(event);

        return RequestMapper.toEventRequestDto(currentEventRequest);
    }

    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with ID = %s wasn't found", eventId)));
    }

    private boolean isRequestLimitReached(Event event) {

        long limitConfirmed = event.getRequests().stream().filter(e -> e.getStatus().equals(RequestStatus.CONFIRMED)).count();
        return limitConfirmed >= event.getParticipantLimit();
    }

    private void checkChanges(EventDtoInfo eventDtoInfo, Event event) {

        if (eventDtoInfo.getAnnotation() != null) {
            event.setAnnotation(eventDtoInfo.getAnnotation());
        }
        if (eventDtoInfo.getCategory() != null) {
            event.setCategory(Category.builder().id(eventDtoInfo.getCategory()).build());
        }
        if (eventDtoInfo.getDescription() != null) {
            event.setDescription(eventDtoInfo.getDescription());
        }
        if (eventDtoInfo.getEventDate() != null) {
            event.setEventDate(eventDtoInfo.getEventDate());
        }
        if (eventDtoInfo.getPaid() != null) {
            event.setPaid(eventDtoInfo.getPaid());
        }
        if (eventDtoInfo.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDtoInfo.getParticipantLimit());
        }
        if (eventDtoInfo.getTitle() != null) {
            event.setTitle(eventDtoInfo.getTitle());
        }
    }

    private EventFullDto makeFullDto(Event event) {
        return EventMapper.toEventFullDto(event, eventRequestRepository
                        .countAllByStatusAndEventId(RequestStatus.CONFIRMED, event.getId()),
                statClientService.getViewsForEvent(event, false));
    }

    public List<EventDto> toListEventDto(List<Event> events, Boolean uniqueRequests) {

        List<Long> evs = events.stream().map(Event::getId).collect(Collectors.toList());
        Map<Long, Long> requests = new HashMap<>();
        for (Long[] temp : eventRepository
                .countAllByStatusAndEventIdIn(RequestStatus.CONFIRMED.name(), evs)) {
            requests.put(temp[0], temp[1]);
        }
        Map<Long, Long> views = statClientService.getViewsForEvents(events, uniqueRequests);

        return events.stream()
                .map(event -> EventMapper.toEventDto(event,
                        views.get(event.getId()),
                        requests.get(Objects.isNull(event.getId()) ? 0 : event.getId())))
                .collect(Collectors.toList());
    }

    public List<EventFullDto> toListEventFullDto(List<Event> events, Boolean uniqueRequests) {

        List<Long> evs = events.stream().map(Event::getId).collect(Collectors.toList());
        Map<Long, Long> requests = new HashMap<>();
        for (Long[] temp : eventRepository
                .countAllByStatusAndEventIdIn(RequestStatus.CONFIRMED.name(), evs)) {
            requests.put(temp[0], temp[1]);
        }
        Map<Long, Long> views = statClientService.getViewsForEvents(events, uniqueRequests);

        return events.stream()
                .map(event -> EventMapper.toEventFullDto(event,
                        requests.get(Objects.isNull(event.getId()) ? 0 : event.getId()),
                        views.get(event.getId())))
                .collect(Collectors.toList());
    }
}
