package de.micromata.jonas.project.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class UserDTO {
    @NonNull
    private String name;

    private String jwt;

}
