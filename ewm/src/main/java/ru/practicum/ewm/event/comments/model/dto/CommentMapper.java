package ru.practicum.ewm.event.comments.model.dto;

import ru.practicum.ewm.event.comments.model.Comment;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.User;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .created(comment.getCreated())
                .user(comment.getUser().getId())
                .event(comment.getEvent().getId())
                .text(comment.getText())
                .likesCount(comment.getLikesCount())
                .dislikesCount(comment.getDislikesCount())
                .build();
    }

    public static Comment toComment(CommentDto commentDto, User user, Event event) {
        return Comment.builder()
                .id(commentDto.getId())
                .created(commentDto.getCreated())
                .user(user)
                .event(event)
                .text(commentDto.getText())
                .likesCount(commentDto.getLikesCount())
                .dislikesCount(commentDto.getDislikesCount())
                .build();
    }
}
