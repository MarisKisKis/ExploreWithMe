package eventRequest;

import event.model.Event;
import user.User;

public class RequestMapper {

    public static RequestDto toEventRequestDto(EventRequest eventRequest) {
        return RequestDto.builder()
                .id(eventRequest.getId())
                .requester(eventRequest.getUser().getId())
                .event(eventRequest.getEvent().getId())
                .status(eventRequest.getStatus())
                .created(eventRequest.getCreated())
                .build();
    }

    public static EventRequest toEventRequest(RequestDto eventRequestDto, User user, Event event) {
        return EventRequest.builder()
                .id(eventRequestDto.getId())
                .user(user)
                .event(event)
                .status(eventRequestDto.getStatus())
                .build();
    }
}
