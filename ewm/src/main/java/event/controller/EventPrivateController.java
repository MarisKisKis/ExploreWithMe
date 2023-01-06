package event.controller;

import event.EventService;
import event.dto.EventDto;
import event.dto.EventDtoInfo;
import event.dto.EventFullDto;
import eventRequest.RequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {

    @Autowired
    private final EventService eventService;

    @GetMapping
    public List<EventDto> getEventsByCreator(@PathVariable Long userId,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get events by creator with ID = {}", userId);
        return eventService.getEventsByCreator(userId, from, size);
    }

    @PatchMapping
    public EventFullDto updateEventByCreator(@PathVariable @NotNull Long userId,
                                             @RequestBody EventDtoInfo eventDtoInfo) {
        log.info("Update event {} by creator with ID = {}", eventDtoInfo, userId);
        return eventService.updateEventByCreator(userId, eventDtoInfo);
    }

    @PostMapping
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @RequestBody @Valid EventDtoInfo eventDtoInfo) {
        log.info("Create event {} by creator with ID = {}", eventDtoInfo, userId);
        return eventService.createEvent(userId, eventDtoInfo);
    }

    @GetMapping("{eventId}")
    public EventFullDto getEventByIdForCreator(@PathVariable Long userId,
                                                    @PathVariable Long eventId) {
        log.info("Get event with ID = {}", eventId);
        return eventService.getEventByIdForCreator(userId, eventId);
    }

    @PatchMapping("{eventId}")
    public EventFullDto cancelEventByCreator(@PathVariable @NotNull Long userId,
                                             @PathVariable @NotNull Long eventId) {
        log.info("Cancel event with ID = {} by creator", eventId);
        return eventService.cancelEventByCreator(userId, eventId);
    }

    @GetMapping("{eventId}/requests")
    public List<RequestDto> getRequestEventByUser(@PathVariable Long userId,
                                                       @PathVariable Long eventId) {
        log.info("Get info for event's request by user with ID = {} for event with ID = {}", userId, eventId);
        return eventService.getRequestEventByUser(userId, eventId);
    }

    @PatchMapping("{eventId}/requests/{reqId}/confirm")
    public RequestDto confirmRequestForEvent(@PathVariable @NotNull Long userId,
                                                  @PathVariable @NotNull Long eventId,
                                                  @PathVariable @NotNull Long reqId) {
        log.info("Confirm request for event with ID = {} for user with ID = {}", eventId, userId);
        return eventService.confirmRequestForEvent(userId, eventId, reqId);
    }

    @PatchMapping("{eventId}/requests/{reqId}/reject")
    public RequestDto rejectRequestForEvent(@PathVariable @NotNull Long userId,
                                                 @PathVariable @NotNull Long eventId,
                                                 @PathVariable @NotNull Long reqId) {
        log.info("Reject request for event with ID = {} for user with ID = {}", eventId, userId);
        return eventService.rejectRequestForEvent(userId, eventId, reqId);
    }
}
