package de.micromata.jonas.project.service;

import de.micromata.jonas.project.dtos.CommentDTO;
import de.micromata.jonas.project.exception.InternalServerException;
import de.micromata.jonas.project.models.Comment;
import de.micromata.jonas.project.models.User;
import de.micromata.jonas.project.repositorys.CommentRepository;
import de.micromata.jonas.project.repositorys.LikeRepository;
import de.micromata.jonas.project.repositorys.TopicRepository;
import de.micromata.jonas.project.repositorys.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CommentService {


    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private LikeRepository likeRepository;

    private static final Logger LOG = LoggerFactory.getLogger(CommentService.class);

    public Comment convertCommentDTO(CommentDTO commentDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LOG.info(user.getName());
        var topic = topicRepository.findByTopicIgnoreCase(commentDTO.getTopic());
        if (user != null || topic != null) {
            var comment = new Comment(commentDTO.getComment(), user, new Date(), topic);
            return comment;

        }
        throw new InternalServerException("ConvertCommentFail");
    }

    public List<CommentDTO> getCommentDTOList(String topicTopic) {
        var topic = topicRepository.findByTopicIgnoreCase(topicTopic);
        var comments = commentRepository.findAllByTopic(topic);
        ArrayList<CommentDTO> commentDTOs = new ArrayList<>();

        for (Comment comment : comments) {
            try {
                CommentDTO commentDTO = convertComment(comment);
                // Query Mongo Abfrage
                int t = likeRepository.countAllByLiked(comment);
                System.out.println(t);
                commentDTO.setLikes(t);
                commentDTOs.add(commentDTO);

                LOG.info("Likes:" + commentDTO.getLikes());
            } catch (Exception e) {
                LOG.error("Convert fail", e);
            }

        }
        Collections.reverse(commentDTOs);

        LOG.info("All Comments loaded ");
        return commentDTOs;

    }

    public void addComment(CommentDTO commentDTO) {

        try{
        commentRepository.save(convertCommentDTO(commentDTO));
        LOG.info("Comment was added :{}",commentDTO.getComment());
    }catch (Exception e){
        LOG.error("Comment save failed :" ,e);
    }
    }

    private CommentDTO convertComment(Comment comment) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String date = dateFormat.format(comment.getDate());


        var commentDTO = new CommentDTO(comment.getComment(), date, comment.getTopic().getTopic(), comment.getId());
        commentDTO.setName(comment.getUser().getName());
        return commentDTO;

    }


    public List<CommentDTO> getSearchDTOListByUserName(String name, String topicTopic) {
        var topic = topicRepository.findByTopicIgnoreCase(topicTopic);
        var users = userRepository.findAllByNameStartingWithIgnoreCase(name);
        ArrayList<CommentDTO> commentDTOs = new ArrayList<>();
        //Topic
        for (User user : users) {
            var comments = commentRepository.findAllByUser(user);
            for (Comment comment : comments) {
                try {
                    if (topic.getTopic().equalsIgnoreCase(comment.getTopic().getTopic()))
                        if (comment.getUser().getName().toLowerCase(Locale.forLanguageTag("UTF-8")).startsWith(name.toLowerCase(Locale.forLanguageTag("UTF-8")))) {
                            commentDTOs.add(convertComment(comment));
                        }
                } catch (Exception e) {
                    LOG.error("Convert fail", e);
                }

            }
        }
        commentDTOs.sort(Comparator.comparing(CommentDTO::getName));
        return commentDTOs;
    }


}



