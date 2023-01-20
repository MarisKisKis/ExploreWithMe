package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventStatus;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.user.User;

import java.time.LocalDateTime;

public class EventMapper {

    public static EventFullDto toEventFullDto(Event event, Long confirmedRequests, Long views) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .category(EventFullDto.CategoryDto.builder()
                        .id(event.getCategory().getId())
                        .name(event.getCategory().getName())
                        .build())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                 .requestModeration(event.getRequestModeration())
                .participantLimit(event.getParticipantLimit())
                .initiator(EventFullDto.UserShortDto.builder()
                        .id(event.getInitiator().getId())
                        .name(event.getInitiator().getName())
                        .build())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .state(event.getState())
                .location(EventFullDto.Location.builder()
                        .lat(event.getLocation().getLat())
                        .lon(event.getLocation().getLon())
                        .build())
                .views(views)
                .confirmedRequests(Math.toIntExact(confirmedRequests))
                .build();
    }

    public static Event toEvent(Long userId, EventDtoInfo eventDtoInfo) {
        return Event.builder()
                .id(eventDtoInfo.getEventId())
                .title(eventDtoInfo.getTitle())
                .annotation(eventDtoInfo.getAnnotation())
                .description(eventDtoInfo.getDescription())
                .category(Category.builder()
                        .id(eventDtoInfo.getCategory())
                        .build())
                .initiator(User.builder()
                        .id(userId)
                        .build())
                .createdOn(LocalDateTime.now())
                .state(EventStatus.PENDING)
                .eventDate(eventDtoInfo.getEventDate())
                .location(Location.builder()
                        .lat(eventDtoInfo.getLocation().getLat())
                        .lon(eventDtoInfo.getLocation().getLon())
                        .build())
                .paid(eventDtoInfo.getPaid())
                .participantLimit(eventDtoInfo.getParticipantLimit())
                .requestModeration(eventDtoInfo.getRequestModeration())
                .build();
    }

    public static EventDto toEventDto(Event event, Long views, Long reqs) {
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(EventDto.CategoryDto.builder()
                        .id(event.getCategory().getId())
                        .name(event.getCategory().getName())
                        .build())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(EventDto.UserShortDto.builder()
                        .id(event.getInitiator().getId())
                        .name(event.getInitiator().getName())
                        .build())
                .paid(event.getPaid())
                .views(views)
                .confirmedRequests(Math.toIntExact(reqs))
                .title(event.getTitle())
                .build();
    }

    public static EventDto toEventDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(EventDto.CategoryDto.builder()
                        .id(event.getCategory().getId())
                        .name(event.getCategory().getName())
                        .build())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(EventDto.UserShortDto.builder()
                        .id(event.getInitiator().getId())
                        .name(event.getInitiator().getName())
                        .build())
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }
}
