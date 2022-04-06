package de.micromata.jonas.project;

import de.micromata.jonas.project.dtos.Response;
import de.micromata.jonas.project.dtos.UserDTO;
import de.micromata.jonas.project.dtos.UserLoginDTO;
import de.micromata.jonas.project.dtos.UserRegisterDTO;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureDataMongo // Erst Spring Boot Test, dann die MongoDB
@SpringBootTest(classes = {ProjectApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TopicRepository topicRepository;

    @LocalServerPort
    int randomServerPort;

    @BeforeEach
    void init() {
        mongoTemplate.getCollection("user").drop();
        mongoTemplate.getCollection("comment").drop();
        User user = new User("jonas", "12345678", new Date());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        Topic topic = new Topic(user, new Date(), "Bannane");
        topicRepository.save(topic);
        topic = topicRepository.findByTopicIgnoreCase("Bannane");
        commentRepository.save(new Comment("hallo", user, new Date(), topic));
    }

    @Test
    void login_ExistingUser_OK() {
        UserLoginDTO user = new UserLoginDTO("jonas", "12345678");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity httpEntity = new HttpEntity(user);
        ResponseEntity<UserDTO> result = restTemplate.exchange("http://localhost:" + randomServerPort + "/api/user/login", HttpMethod.POST, httpEntity, UserDTO.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getName()).isEqualTo(user.getName());

    }

    @Test
    void login_NoExistingUser_BadRequest() {
        User user = new User("peter", "12345678", new Date());
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity httpEntity = new HttpEntity(user);
        try {
            ResponseEntity<UserDTO> result = restTemplate.exchange("http://localhost:" + randomServerPort + "/api/user/login", HttpMethod.POST, httpEntity, UserDTO.class);
        } catch (HttpClientErrorException exception) {
            assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(exception.getMessage()).contains("username and passwort not macht");
        }
    }

    @Test
    void register_NoExistingUser_ReturnHttpStatusOKWithUser() {
        UserRegisterDTO user = new UserRegisterDTO("hannes", "12345678", new Date());
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<User> httpEntity = new HttpEntity(user);
        ResponseEntity<UserDTO> result = restTemplate.exchange("http://localhost:" + randomServerPort + "/api/user/register", HttpMethod.POST, httpEntity, UserDTO.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getName()).isEqualTo(user.getName());
    }

    @Test
    void register_NoExistingUserWithoutPassword_ReturnHttpStatusBadRequest() {
        User user = new User("herbert", "", new Date());
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<User> httpEntity = new HttpEntity(user);
        try {
            ResponseEntity<Response> result = restTemplate.exchange("http://localhost:" + randomServerPort + "/api/user/register", HttpMethod.POST, httpEntity, Response.class);
        } catch (HttpClientErrorException exception) {
            assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(exception.getMessage()).contains("No password has been entered");
        }
    }

    @Test
    void register_NoExistingUserWithToShortPasswoerd_ReturnHttpStatusBadRequest() {
        User user = new User("herbert", "12", new Date());
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<User> httpEntity = new HttpEntity(user);
        try {
            ResponseEntity<Response> result = restTemplate.exchange("http://localhost:" + randomServerPort + "/api/user/register", HttpMethod.POST, httpEntity, Response.class);
        } catch (HttpClientErrorException exception) {
            assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(exception.getMessage()).contains("Password must contain at least 8 characters");
        }
    }

    @Test
    void register_NoExistingUserWithoutName_ReturnHttpStatusBadRequest() {
        User user = new User("", "124545465", new Date());
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<User> httpEntity = new HttpEntity(user);
        try {
            ResponseEntity<Response> result = restTemplate.exchange("http://localhost:" + randomServerPort + "/api/user/register", HttpMethod.POST, httpEntity, Response.class);
        } catch (HttpClientErrorException exception) {
            assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(exception.getMessage()).contains("Username must contain at least 4 characters");
        }
    }

    @Test
    void register_NoExistingUserWithoutBDay_ReturnHttpStatusBadRequest() {
        User user = new User("ichMagPommes", "1sdsdsd2", null);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<User> httpEntity = new HttpEntity(user);
        try {
            ResponseEntity<Response> result = restTemplate.exchange("http://localhost:" + randomServerPort + "/api/user/register", HttpMethod.POST, httpEntity, Response.class);
        } catch (HttpClientErrorException exception) {
            assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(exception.getMessage()).contains("No B-Day has been entered");
        }
    }


}
