package de.micromata.jonas.project.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class UserRegisterDTO {
    @NonNull
    private String name;
    @NonNull
    private String password;
    @NonNull
    private Date birthday;
    
}
