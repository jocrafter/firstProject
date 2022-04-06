package de.micromata.jonas.project.repositorys;

import de.micromata.jonas.project.models.Comment;
import de.micromata.jonas.project.models.Topic;
import de.micromata.jonas.project.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

 List<Comment> findAllByUser(User user);

 List<Comment> findAllByTopic(Topic topic);


}