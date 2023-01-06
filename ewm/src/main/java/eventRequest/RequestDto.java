package eventRequest;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.extern.jackson.Jacksonized;


import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Jacksonized
public class RequestDto {

    private Long id;
    private Long event;
    private Long requester;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    private RequestStatus status;
}
