package de.micromata.jonas.project.controller;


import de.micromata.jonas.project.JwtUtil;
import de.micromata.jonas.project.dtos.CommentDTO;
import de.micromata.jonas.project.dtos.Response;
import de.micromata.jonas.project.exception.InternalServerException;
import de.micromata.jonas.project.models.User;
import de.micromata.jonas.project.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CommentService commentService;

    private static final Logger LOG = LoggerFactory.getLogger(CommentController.class);


    /**
     * This Method Get a Comment Object from  myscript and
     * add the Object to commentList and Log the Message "Comment is" Comment
     *
     * @param commentDTO
     * @return
     */

    @PostMapping("/{topic}")
    public ResponseEntity postComment(@RequestBody CommentDTO commentDTO, @PathVariable String topic) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        commentDTO.setComment(StringUtils.trim(commentDTO.getComment()));

        if (StringUtils.isBlank(commentDTO.getComment())) {
            LOG.error("Invalid request from user: {}: Empty Comment", user.getName());
            throw new InternalServerException("Comment is Empty");

        }
        if (StringUtils.isBlank(topic)) {
            throw new InternalServerException("Topic is Empty");

        }
        LOG.info("Comment name is : {}", commentDTO.getName());
        LOG.info("Comment is : {}", commentDTO.getComment());
        LOG.info("asfas{}", commentDTO.getName());
        commentDTO.setTopic(topic);
        commentService.addComment(commentDTO);
        LOG.info("asfas{}", commentDTO.getName());
        return ResponseEntity.status(HttpStatus.OK).body(new Response(commentDTO.getComment()));

    }

    /**
     * Get the List of Comments on the GetMap /comments
     *
     * @return commentList
     */
    @GetMapping("/{topic}/")
    public List<CommentDTO> getComment(@PathVariable String topic, @RequestParam(name = "search") String searchName) {
        searchName = StringUtils.trim(searchName);
        LOG.info("Load Commends from Topic : {} ...", topic);
        if (!StringUtils.isBlank(searchName)) {
            return commentService.getSearchDTOListByUserName(searchName, topic);
        }
        return commentService.getCommentDTOList(topic);


    }


}
