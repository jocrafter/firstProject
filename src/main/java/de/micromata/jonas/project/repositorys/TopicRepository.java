package de.micromata.jonas.project.repositorys;

import de.micromata.jonas.project.models.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends MongoRepository<Topic, String> {
    
    List<Topic> findAllByTopicStartingWithIgnoreCase(String topic);

    Topic findByTopicIgnoreCase(String string);

    
}
