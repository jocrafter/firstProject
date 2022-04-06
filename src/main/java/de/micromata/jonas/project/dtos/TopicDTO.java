package de.micromata.jonas.project.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class TopicDTO {

    private String user;
    @NonNull
    private String topic;
    @NonNull
    private String description;
    @NonNull
    private String date;

    private String pictureName;

    private String smallPicture;

    @NonNull
    private String id;

    private int likes;


}
