package event;

import event.model.Event;
import event.model.EventStatus;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long userId);

    Set<Event> findAllByIdIn(Set<Long> events);

    Optional<Event> findByIdAndState(Long eventId, EventStatus state);

    @Query("SELECT e FROM Event AS e " +
            "WHERE ((:users) IS NULL OR e.initiator.id IN :users) " +
            "AND ((:states) IS NULL OR e.state IN :states) " +
            "AND ((:catIds) IS NULL OR e.category.id IN :catIds) " +
            "AND (e.eventDate >= :rangeStart) " +
            "AND (CAST(:rangeEnd AS date) IS NULL OR e.eventDate <= :rangeEnd)")
    List<Event> searchEvents(List<Long> users, List<EventStatus> states,
                             List<Integer> catIds, LocalDateTime rangeStart,
                             LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e where (lower(e.annotation) like lower(concat('%', :text, '%')) or " +
            "lower(e.description) like lower(concat('%', :text, '%')))" +
            "AND ((:catIds) IS NULL OR e.category.id IN :catIds) " +
            "and e.paid = :isPaid " +
            "and e.eventDate between :rangeStart and :rangeEnd " +
            "and (:onlyAvailable is false or (e.participantLimit = 0 or e.confirmedRequests < e.participantLimit))")
    List<Event> findEvents(String text, List<Integer> catIds, Boolean isPaid, LocalDateTime rangeStart,
                           LocalDateTime rangeEnd, Boolean onlyAvailable);


    @Query(value = "select ev.id, (select count(er.id) " +
            "from events e " +
            "left join event_requests er on e.id = er.event_id " +
            "where er.status = :status " +
            "and e.id in :eventIds) " +
            "from events ev " +
            "where ev.id in :eventIds  " +
            "group by ev.id", nativeQuery = true)
    List<Long[]> countAllByStatusAndEventIdIn(@Param("status") String status,
                                              @Param("eventIds") List<Long> eventIds);
}
