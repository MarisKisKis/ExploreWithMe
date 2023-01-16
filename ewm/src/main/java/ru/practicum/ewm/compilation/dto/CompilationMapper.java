package ru.practicum.ewm.compilation.dto;

import ru.practicum.ewm.compilation.Compilation;
import ru.practicum.ewm.event.model.Event;

import java.util.stream.Collectors;

public class CompilationMapper {

    public static CompilationDto toCompilDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents()
                        .stream()
                        .map(CompilationMapper::toEventDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    private static CompilationDto.EventShortDto toEventDto(Event event) {
        return CompilationDto.EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CompilationDto.EventShortDto.CategoryDto.builder()
                        .id(event.getCategory().getId())
                        .name(event.getCategory().getName())
                        .build())
                .eventDate(event.getEventDate())
                .initiator(CompilationDto.EventShortDto.UserShortDto.builder()
                        .id(event.getInitiator().getId())
                        .name(event.getInitiator().getName())
                        .build())
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public static Compilation toCompilation(CompilationInfoDto compilationInfoDto) {
        return Compilation.builder()
                .pinned(compilationInfoDto.getPinned())
                .title(compilationInfoDto.getTitle())
                .events(compilationInfoDto.getEvents()
                        .stream()
                        .map(e -> Event.builder().id(e).build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
