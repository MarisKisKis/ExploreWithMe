package ru.practicum.ewm.event.comments.model;

import lombok.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @NotNull
    @Column(name = "text")
    private String text;

    @NotNull
    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "likes_count")
    @Builder.Default
    private Integer likesCount = 0;

    @Column(name = "dislikes_count")
    @Builder.Default
    private Integer dislikesCount = 0;
}
