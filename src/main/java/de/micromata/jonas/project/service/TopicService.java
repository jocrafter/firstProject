package de.micromata.jonas.project.service;

import de.micromata.jonas.project.dtos.TopicDTO;
import de.micromata.jonas.project.exception.IamATeaPot;
import de.micromata.jonas.project.exception.NotFoundException;
import de.micromata.jonas.project.models.Like;
import de.micromata.jonas.project.models.Picture;
import de.micromata.jonas.project.models.Topic;
import de.micromata.jonas.project.models.User;
import de.micromata.jonas.project.repositorys.LikeRepository;
import de.micromata.jonas.project.repositorys.TopicRepository;
import de.micromata.jonas.project.repositorys.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TopicService {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private LikeRepository likeRepository;
    private static final Logger LOG = LoggerFactory.getLogger(TopicService.class);

    public TopicDTO addTopic(TopicDTO topicDTO) throws IOException {
        if (topicRepository.findByTopicIgnoreCase(topicDTO.getTopic()) != null) {
            throw new IamATeaPot("Allways Exist");
        }
        var topic = convertToTopic(topicDTO);

        if (topic.getPicture() != null) {
            topic.setSmallPicture(pictureService.loadSmallFileByPictureName(topic.getPicture().getName()));
        }
        topicRepository.save(topic);
        topicDTO.setId(topic.getId());

        return topicDTO;

    }

    public Topic convertToTopic(TopicDTO topicDTO) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Topic topic = new Topic(user, new Date(), topicDTO.getTopic());
        topic.setDescription(topicDTO.getDescription());

        if (topicDTO.getPictureName() != null) {

            Picture picture = pictureService.getPicture(topicDTO.getPictureName());
            topic.setPicture(picture);
        }


        return topic;
    }

    public TopicDTO convertToTopicDTO(Topic topic) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String date = dateFormat.format(topic.getDate());
        ArrayList<Like> likes = likeRepository.findAllByLiked(topic);
        TopicDTO t = new TopicDTO(topic.getTopic(), topic.getDescription(), date, topic.getId());
        if (topic.getPicture() != null) {
            try {
                topic.setSmallPicture(pictureService.loadSmallFileByPictureName(topic.getPicture().getName()));
                t.setPictureName(topic.getPicture().getName());
                t.setSmallPicture(topic.getSmallPicture());
                System.out.println(topic.getSmallPicture());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        

        t.setLikes(likes.size());
        return t;
    }

    public ResponseEntity getTopicDTOList(String filter) {

        List<Topic> topics = topicRepository.findAllByTopicStartingWithIgnoreCase(filter);

        List<TopicDTO> topicDTOs = new ArrayList<>();

        for (Topic topic : topics) {

            TopicDTO topicDTO = convertToTopicDTO(topic);
            topicDTOs.add(topicDTO);
            if (topicDTOs.size() == 7)
                break;
        }
        return ResponseEntity.status(HttpStatus.OK).body(topicDTOs);
    }

    public TopicDTO getTopic(String topicTopic) {
        var topic = topicRepository.findByTopicIgnoreCase(topicTopic);
        if (topic == null) {
            throw new NotFoundException("Topic is not found");
        }
        var topicDTO = convertToTopicDTO(topic);
        return topicDTO;

    }
}
