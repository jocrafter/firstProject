package de.micromata.jonas.project.service;

import de.micromata.jonas.project.dtos.Response;
import de.micromata.jonas.project.models.Comment;
import de.micromata.jonas.project.models.Like;
import de.micromata.jonas.project.models.Topic;
import de.micromata.jonas.project.models.User;
import de.micromata.jonas.project.repositorys.CommentRepository;
import de.micromata.jonas.project.repositorys.LikeRepository;
import de.micromata.jonas.project.repositorys.TopicRepository;
import de.micromata.jonas.project.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LikeService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    TopicRepository topicRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LikeRepository likeRepository;


    public ResponseEntity addLikeTopic(String id) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Topic topic = topicRepository.findById(id).get();
        topic.setLikes(likeRepository.findAllByLiked(topic));

        Like like = new Like(user, topic, new Date());
        topic.addLike(like);
        System.out.println(!likeRepository.existsByUserAndLiked(user, topic));
        if (!likeRepository.existsByUserAndLiked(user, topic)) {

            likeRepository.save(like);
            String i = likeRepository.countAllByLiked(topic) + "";
            System.out.println(i);
            return ResponseEntity.status(HttpStatus.OK).body(new Response(i));

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("always likes"));


        
    

     
       /*
        Update update = new Update();
        update.addToSet("liks", like);
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(user.getId()));
        userRepository.update(Query.query(criteria),update,"haha");
        */


    }

    public ResponseEntity addLikeComment(String id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = commentRepository.findById(id).get();
        comment.setLikes(likeRepository.findAllByLiked(comment));
        Like like = new Like(user, comment, new Date());
        comment.addLike(like);
        if (!likeRepository.existsByUserAndLiked(user, comment)) {

            likeRepository.save(like);
            String i = likeRepository.countAllByLiked(comment) + "";
            System.out.println(i);
            return ResponseEntity.status(HttpStatus.OK).body(new Response(i));

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("always likes"));


    }


}
