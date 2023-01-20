package ru.practicum.ewm.eventRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class EventRequestController {

    @Autowired
    private final RequestService eventRequestService;

    @GetMapping
    public List<RequestDto> getRequests(@PathVariable Long userId) {
        log.info("Get all requests for ru.practicum.user with ID = {}", userId);
        return eventRequestService.getRequests(userId);
    }

    @PostMapping
    public RequestDto addRequest(@PathVariable @NotNull Long userId,
                                      @RequestParam Long eventId) {
        log.info("Create request for ru.practicum.user with ID = {} for ru.practicum.event with ID = {}", userId, eventId);
        return eventRequestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable @NotNull Long userId,
                                         @PathVariable @NotNull Long requestId) {
        log.info("Cancel request with ID = {}", requestId);
        return eventRequestService.cancelRequest(userId, requestId);
    }
}
