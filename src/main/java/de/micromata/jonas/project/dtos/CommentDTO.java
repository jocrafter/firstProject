package de.micromata.jonas.project.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class CommentDTO {
    @NonNull
    private String comment;
    @NonNull
    private String date;
    @NonNull
    private String topic;

    private int likes;
    private String name;

    @NonNull
    private String id;

}
