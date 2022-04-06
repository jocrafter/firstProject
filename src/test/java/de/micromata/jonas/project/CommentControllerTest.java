package de.micromata.jonas.project;

import de.micromata.jonas.project.dtos.CommentDTO;
import de.micromata.jonas.project.models.Comment;
import de.micromata.jonas.project.models.Topic;
import de.micromata.jonas.project.models.User;
import de.micromata.jonas.project.repositorys.CommentRepository;
import de.micromata.jonas.project.repositorys.TopicRepository;
import de.micromata.jonas.project.repositorys.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureDataMongo // Erst Spring Boot Test, dann die MongoDB
@SpringBootTest(classes = {ProjectApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class CommentControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TopicRepository topicRepository;

    @LocalServerPort
    int randomServerPort;

    @BeforeEach
    void init() {
        mongoTemplate.getCollection("user").drop();
        mongoTemplate.getCollection("comment").drop();
        User user = new User("jonas", "12345678", new Date());
        userRepository.save(user);
        Topic topic = new Topic(user, new Date(), "Bannane");
        topicRepository.save(topic);
        topic = topicRepository.findByTopicIgnoreCase("Bannane");
        commentRepository.save(new Comment("hallo", user, new Date(), topic));
    }

    @Test
    void getComment_NoInput_ReturnComments() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity httpEntity = new HttpEntity("");
        ResponseEntity<List> result = restTemplate.exchange("http://localhost:" + randomServerPort + "/api/comment/comments", HttpMethod.GET, httpEntity, List.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().size()).isEqualTo(1);

    }

    @Test
    void postComment_Comment_OK() {
        CommentDTO commentDTO = new CommentDTO("Hallo", "31.10.2008 15:30:10", "Bannane", "afsjnolö");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity httpEntity = new HttpEntity(commentDTO);
        HttpEntity httpEntity1 = new HttpEntity("");
        ResponseEntity result = restTemplate.exchange("http://localhost:" + randomServerPort + "/api/comment/comment", HttpMethod.POST, httpEntity, CommentDTO.class);
        ResponseEntity<List> resultgetComments = restTemplate.exchange("http://localhost:" + randomServerPort + "/api/comment/comments", HttpMethod.GET, httpEntity1, List.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Arrays.asList(resultgetComments.getBody()).stream().filter(com -> commentDTO.getComment().equals(commentDTO.getComment())).count()).isGreaterThan(0);


    }


}
