package de.micromata.jonas.project.controller;

import de.micromata.jonas.project.dtos.TopicDTO;
import de.micromata.jonas.project.repositorys.TopicRepository;
import de.micromata.jonas.project.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private TopicRepository topicRepository;


    @PostMapping
    public ResponseEntity newTopic(@RequestBody TopicDTO topicDTO) throws Exception {
        topicService.addTopic(topicDTO);
        return ResponseEntity.status(HttpStatus.OK).body(topicDTO);

    }

    @GetMapping("/{topic}")
    public TopicDTO getTopic(@PathVariable String topic) {
        return topicService.getTopic(topic);

    }

    @GetMapping("/filtered")
    public ResponseEntity getTopics(@RequestParam(name = "topic") String topic) {
        return topicService.getTopicDTOList(topic);
    }

    @DeleteMapping("/{id}")
    public void deleteTopic(@PathVariable String id) {

        topicRepository.deleteById(id);
    }
}
