package event.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import event.model.EventStatus;
import lombok.*;
import lombok.extern.jackson.Jacksonized;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Jacksonized
public class EventFullDto {

    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    private Integer confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime eventDate;
    private Long id;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private Location location;
    @NotNull
    private Boolean paid;
    private Integer participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventStatus state;
    @NotNull
    private String title;
    private Long views;

    @Getter
    @Setter
    @Builder
    @Jacksonized
    public static class CategoryDto {
        private Integer id;
        private String name;
    }

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
    public static class Location {
        private Double lat;
        private Double lon;
    }
}
