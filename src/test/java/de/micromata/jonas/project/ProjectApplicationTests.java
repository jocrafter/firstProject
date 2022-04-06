package de.micromata.jonas.project;

/*
@SpringBootTest(classes = {ProjectApplication.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
class ProjectApplicationTests {

    @Autowired
    private HelloController helloController;

    @LocalServerPort
    int randomServerPort;

    @BeforeEach
    void init() {
        helloController.init();
    }

    @Test
    void getComment_NoInput_ReturnComments() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*//*");
        HttpEntity<Comment[]> httpEntity = new HttpEntity("", headers);
        ResponseEntity<Comment[]> result = restTemplate.exchange("http://localhost:" + randomServerPort + "/comments", HttpMethod.GET, httpEntity, Comment[].class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).hasSize(3);

    }

    @Test
    void postComment_Comment_OK() {
        Comment comment = new Comment("Hallo", "jonas");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*//*");
        HttpEntity<Comment> httpEntity = new HttpEntity(comment, headers);
        ResponseEntity result = restTemplate.exchange("http://localhost:" + randomServerPort + "/comment", HttpMethod.POST, httpEntity, Comment.class);
        ResponseEntity<Comment[]> resultgetComments = restTemplate.exchange("http://localhost:" + randomServerPort + "/comments", HttpMethod.GET, httpEntity, Comment[].class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Arrays.asList(resultgetComments.getBody()).stream().filter(com -> com.getComment().equals(comment.getComment())).count()).isGreaterThan(0);


    }

    @Test
    void login_ExistingUser_OK() {
        User user = new User("jonas", new Date(), "12345678");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*//*");
        HttpEntity<User> httpEntity = new HttpEntity(user, headers);
        ResponseEntity<User> result = restTemplate.exchange("http://localhost:" + randomServerPort + "/login", HttpMethod.POST, httpEntity, User.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getName()).isEqualTo(user.getName());

    }

    @Test
    void login_NoExistingUser_BadRequest() {
        User user = new User("peter", new Date(), "12345678");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*//*");
        HttpEntity<User> httpEntity = new HttpEntity(user, headers);
        try {
            ResponseEntity<Response> result = restTemplate.exchange("http://localhost:" + randomServerPort + "/login", HttpMethod.POST, httpEntity, Response.class);
        } catch (HttpClientErrorException exception) {
            assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(exception.getMessage()).contains("username and passwort not macht");
        }
    }

    @Test
    void register_NoExistingUser_ReturnHttpStatusOKWithUser() {
        User user = new User("hannes", new Date(), "12345678");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*//*");
        HttpEntity<User> httpEntity = new HttpEntity(user, headers);
        ResponseEntity<User> result = restTemplate.exchange("http://localhost:" + randomServerPort + "/register", HttpMethod.POST, httpEntity, User.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getName()).isEqualTo(user.getName());
    }

    @Test
    void register_NoExistingUserWithoutPassword_ReturnHttpStatusBadRequest() {
        User user = new User("herbert", new Date(), "");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*//*");
        HttpEntity<User> httpEntity = new HttpEntity(user, headers);
        try {
            ResponseEntity<Response> result = restTemplate.exchange("http://localhost:" + randomServerPort + "/register", HttpMethod.POST, httpEntity, Response.class);
        } catch (HttpClientErrorException exception) {
            assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(exception.getMessage()).contains("No password has been entered");
        }
    }

    @Test
    void register_NoExistingUserWithToShortPasswoerd_ReturnHttpStatusBadRequest() {
        User user = new User("herbert", new Date(), "12");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*//*");
        HttpEntity<User> httpEntity = new HttpEntity(user, headers);
        try {
            ResponseEntity<Response> result = restTemplate.exchange("http://localhost:" + randomServerPort + "/register", HttpMethod.POST, httpEntity, Response.class);
        } catch (HttpClientErrorException exception) {
            assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(exception.getMessage()).contains("Password must contain at least 8 characters");
        }
    }

    @Test
    void register_NoExistingUserWithoutName_ReturnHttpStatusBadRequest() {
        User user = new User("", new Date(), "124545465");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*//*");
        HttpEntity<User> httpEntity = new HttpEntity(user, headers);
        try {
            ResponseEntity<Response> result = restTemplate.exchange("http://localhost:" + randomServerPort + "/register", HttpMethod.POST, httpEntity, Response.class);
        } catch (HttpClientErrorException exception) {
            assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(exception.getMessage()).contains("Username must contain at least 4 characters");
        }
    }

    @Test
    void register_NoExistingUserWithoutBDay_ReturnHttpStatusBadRequest() {
        User user = new User("ichMagPommes", null, "1sdsdsd2");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*//*");
        HttpEntity<User> httpEntity = new HttpEntity(user, headers);
        try {
            ResponseEntity<Response> result = restTemplate.exchange("http://localhost:" + randomServerPort + "/register", HttpMethod.POST, httpEntity, Response.class);
        } catch (HttpClientErrorException exception) {
            assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(exception.getMessage()).contains("No B-Day has been entered");
        }
    }

}
*/