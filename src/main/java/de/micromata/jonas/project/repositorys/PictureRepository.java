package de.micromata.jonas.project.repositorys;

import de.micromata.jonas.project.models.Picture;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends MongoRepository<Picture, String> {

    Picture findByName(String name);

}
