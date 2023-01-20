package ru.practicum.ewm.eventRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.user.User;


import java.util.List;

public interface RequestRepository extends JpaRepository<EventRequest, Long> {

    List<EventRequest> findAllByUser(User user);

    List<EventRequest> findAllByEventId(Long eventId);

    Long countAllByStatusAndEventId(RequestStatus requestState, Long eventId);
}
