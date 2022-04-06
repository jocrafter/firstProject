package de.micromata.jonas.project.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class LikeDTO {

    private String user;
    @NonNull
    private String liked;
    private int likes;

}
