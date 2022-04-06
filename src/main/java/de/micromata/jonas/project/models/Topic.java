package de.micromata.jonas.project.models;

import lombok.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "topic")
public class Topic {


    @DBRef
    @Lazy
    private Picture picture;

    @DBRef
    @Lazy
    @NonNull
    private User user;

    @NonNull
    private Date date;

    @NonNull
    private String topic;

    private String smallPicture;

    private String description;

    @DBRef
    @Lazy
    private ArrayList<Like> likes;

    @DBRef
    @Lazy
    private ArrayList<Comment> comments;

    @Id
    private String id;

    public void addLike(Like like) {

        this.likes.add(like);
    }
}
