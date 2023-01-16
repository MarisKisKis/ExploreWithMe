package ru.practicum.ewm.eventRequest;


import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventStatus;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    @Autowired
    private final RequestRepository eventRequestRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final EventRepository eventRepository;

    @Override
    public List<RequestDto> getRequests(Long userId) {

        User user = getUser(userId);
        List<EventRequest> eventRequests = eventRequestRepository.findAllByUser(user);

        return eventRequests.stream()
                .map(RequestMapper::toEventRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto addRequest(Long userId, Long eventId) {

        User user = getUser(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with ID %s wasn't found", eventId)));

        EventRequest eventRequest = EventRequest.builder()
                .event(event)
                .user(user)
                .created(LocalDateTime.now())
                .status(RequestStatus.PENDING)
                .build();


        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Initiator can't be added into ru.practicum.eventRequest");
        }
        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            throw new ValidationException("Event has to be PUBLISHED");
        }
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            eventRequest.setStatus(RequestStatus.CONFIRMED);
        }

        return RequestMapper.toEventRequestDto(eventRequestRepository.save(eventRequest));
    }

    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {

        EventRequest eventRequest = eventRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("EventRequest with ID = %s wasn't found", requestId)));
        getUser(userId);

        eventRequest.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toEventRequestDto(eventRequestRepository.save(eventRequest));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with ID %s wasn't found", userId)));
    }
}
