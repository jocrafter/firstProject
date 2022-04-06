package de.micromata.jonas.project.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "picture")

public class Picture {
    @NonNull
    private String hash;
    @Id
    private String id;
    @NonNull
    private String name;

}
