package de.micromata.jonas.project.repositorys;

import de.micromata.jonas.project.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    User findByNameIgnoreCase(String name);

    int countByNameIgnoreCase(String name);

    List<User> findAllByNameStartingWithIgnoreCase(String string);

}