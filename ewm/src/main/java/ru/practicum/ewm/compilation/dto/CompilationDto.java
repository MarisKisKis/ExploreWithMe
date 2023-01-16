package ru.practicum.ewm.compilation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Jacksonized
public class CompilationDto {

    private Integer id;
    private Boolean pinned;
    private String title;
    @Builder.Default
    private Set<EventShortDto> events = new HashSet<>();

    @Getter
    @Setter
    @Builder
    @Jacksonized
    public static class EventShortDto {

        private String annotation;
        private CategoryDto category;
        private Integer confirmedRequests;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime eventDate;
        private Long id;
        private UserShortDto initiator;
        private Boolean paid;
        private String title;

        @Getter
        @Setter
        @Builder
        @Jacksonized
        public static class UserShortDto {
            private Long id;
            private String name;
        }

        @Getter
        @Setter
        @Builder
        @Jacksonized
        public static class CategoryDto {
            private Integer id;
            private String name;
        }
    }

}
