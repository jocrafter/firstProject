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
@Document(collection = "comment")
public class Comment {

    @NonNull
    private String comment;

    @DBRef
    @NonNull
    private User user;
    @Id
    private String id;

    @NonNull
    private Date date;

    @NonNull
    private Topic topic;

    @DBRef
    @Lazy
    private ArrayList<Like> likes;

    @Override
    public String toString() {
        return "Comment{" +
                "comment='" + comment + '\'' +
                ", user='" + user + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public void addLike(Like like) {

        this.likes.add(like);
    }
}
