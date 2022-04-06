package de.micromata.jonas.project.models;

import lombok.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "like")
public class Like {

    @DBRef
    @Lazy
    @NonNull
    private User user;

    @DBRef
    @NonNull
    private Object liked;

    @NonNull
    private Date date;

    @Id
    private String id;
}
