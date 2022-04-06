package de.micromata.jonas.project.repositorys;


import de.micromata.jonas.project.models.Like;
import de.micromata.jonas.project.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface LikeRepository extends MongoRepository<Like, String> {
    ArrayList<Like> findAllByLiked(Object object);

    int countAllByLiked(Object object);

    boolean existsByUserAndLiked(User user, Object object);

}
