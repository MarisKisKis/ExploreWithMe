package ru.practicum.ewm.event.controller;

import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.comments.model.dto.CommentDto;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventDtoInfo;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.eventRequest.RequestDto;
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
        log.info("Update ru.practicum.event {} by creator with ID = {}", eventDtoInfo, userId);
        return eventService.updateEventByCreator(userId, eventDtoInfo);
    }

    @PostMapping
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @RequestBody @Valid EventDtoInfo eventDtoInfo) {
        log.info("Create ru.practicum.event {} by creator with ID = {}", eventDtoInfo, userId);
        return eventService.createEvent(userId, eventDtoInfo);
    }

    @GetMapping("{eventId}")
    public EventFullDto getEventByIdForCreator(@PathVariable Long userId,
                                                    @PathVariable Long eventId) {
        log.info("Get ru.practicum.event with ID = {}", eventId);
        return eventService.getEventByIdForCreator(userId, eventId);
    }

    @PatchMapping("{eventId}")
    public EventFullDto cancelEventByCreator(@PathVariable @NotNull Long userId,
                                             @PathVariable @NotNull Long eventId) {
        log.info("Cancel ru.practicum.event with ID = {} by creator", eventId);
        return eventService.cancelEventByCreator(userId, eventId);
    }

    @GetMapping("{eventId}/requests")
    public List<RequestDto> getRequestEventByUser(@PathVariable Long userId,
                                                       @PathVariable Long eventId) {
        log.info("Get info for ru.practicum.event's request by ru.practicum.user with ID = {} for ru.practicum.event with ID = {}", userId, eventId);
        return eventService.getRequestEventByUser(userId, eventId);
    }

    @PatchMapping("{eventId}/requests/{reqId}/confirm")
    public RequestDto confirmRequestForEvent(@PathVariable @NotNull Long userId,
                                                  @PathVariable @NotNull Long eventId,
                                                  @PathVariable @NotNull Long reqId) {
        log.info("Confirm request for ru.practicum.event with ID = {} for ru.practicum.user with ID = {}", eventId, userId);
        return eventService.confirmRequestForEvent(userId, eventId, reqId);
    }

    @PatchMapping("{eventId}/requests/{reqId}/reject")
    public RequestDto rejectRequestForEvent(@PathVariable @NotNull Long userId,
                                                 @PathVariable @NotNull Long eventId,
                                                 @PathVariable @NotNull Long reqId) {
        log.info("Reject request for ru.practicum.event with ID = {} for ru.practicum.user with ID = {}", eventId, userId);
        return eventService.rejectRequestForEvent(userId, eventId, reqId);
    }

    @PostMapping("{eventId}/comments")
    public CommentDto addCommentByUser(@PathVariable @NotNull Long userId,
                                         @PathVariable @NotNull Long eventId,
                                         @RequestBody @Valid CommentDto commentDto) {
        log.info("User with ID = {} add a comment for event with ID = {}", userId, eventId);
        return eventService.addCommentByUser(userId, eventId, commentDto);
    }

    @PutMapping("/comments/{commentId}")
    public CommentDto updateCommentByUser(@PathVariable @NotNull Long userId,
                                            @PathVariable @NotNull Long commentId,
                                            @RequestBody @Valid CommentDto commentDto) {
        log.info("User with ID = {} updated comment with ID = {}", userId, commentId);
        return eventService.updateCommentByUser(userId, commentId, commentDto);
    }

    @PutMapping("/comments/{commentId}/like")
    public CommentDto addLikeToComment(@PathVariable @NotNull Long userId,
                                        @PathVariable @NotNull Long commentId) {
        log.info("User with ID = {} updated comment with ID = {}", userId, commentId);
        return eventService.addLikeToComment(userId, commentId);
    }

    @PutMapping("/comments/{commentId}/dislike")
    public CommentDto addDislikeToComment(@PathVariable @NotNull Long userId,
                                           @PathVariable @NotNull Long commentId) {
        log.info("User with ID = {} updated comment with ID = {}", userId, commentId);
        return eventService.addDislikeToComment(userId, commentId);
    }

    @DeleteMapping("/comments/{commentId}")
    public void removeCommentForEvent(@PathVariable @NotNull Long userId,
                                      @PathVariable @NotNull Long commentId) {
        log.info("User with ID = {} removed comment with ID = {}", userId, commentId);
        eventService.removeCommentByUser(userId, commentId);
    }
}
