package ru.practicum.ewm.event.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.comments.model.CommentLink;

public interface CommentLinkRepository extends JpaRepository<CommentLink, Long> {

    CommentLink getByComment_IdAndUser_Id(Long commentId, Long userId);
}
