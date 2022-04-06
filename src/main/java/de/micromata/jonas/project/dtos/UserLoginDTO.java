package de.micromata.jonas.project.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class UserLoginDTO {
    @NonNull
    private String name;
    @NonNull
    private String password;


}