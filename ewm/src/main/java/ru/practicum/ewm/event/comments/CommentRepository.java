package ru.practicum.ewm.event.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.comments.model.Comment;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> getCommentsByEvent_IdOrderByCreatedDesc(Long eventId, Pageable pageable);
}
