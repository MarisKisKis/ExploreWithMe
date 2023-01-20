package ru.practicum.ewm.event.controller;

import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.comments.model.dto.CommentDto;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.model.Sort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventPublicController {

    @Autowired
    private final EventService eventService;

    @Autowired
    private final StatsClient statsClient;

    @GetMapping
    public List<EventDto> getPublicEvents(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) List<Integer> catIds,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(name = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(name = "sort", required = false) String sortStr,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
            HttpServletRequest request) {

        Sort sort = Sort.from(sortStr)
                .orElseThrow(() -> new IllegalArgumentException("Unknown type of sort: " + sortStr));
        log.info("Get events from public");
        statsClient.saveStats(request);
        return eventService.getPublicEvents(text, catIds, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto getPublicEventById(@PathVariable Long id, HttpServletRequest request) {

        log.info("Get ru.practicum.event with ID = {}", id);
        statsClient.saveStats(request);
        return eventService.getPublicEventById(id);
    }

    @GetMapping("/{id}/comments")
    public List<CommentDto> getCommentsForEvent(@PathVariable(name = "id") Long eventId,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                     Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10")
                                                     Integer size) {
        log.info("Retrieve comments for event with ID = {}", eventId);
        return eventService.getCommentsForEvent(eventId, from, size);
    }

    @GetMapping("/comments/{comId}")
    public CommentDto getCommentById(@PathVariable(name = "comId") Long commentId) {
        log.info("Retrieve comment with ID = {}", commentId);
        return eventService.getCommentById(commentId);
    }
}
