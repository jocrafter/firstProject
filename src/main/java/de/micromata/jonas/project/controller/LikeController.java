package de.micromata.jonas.project.controller;

import de.micromata.jonas.project.dtos.Response;
import de.micromata.jonas.project.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/like")
public class LikeController {

    @Autowired
    private LikeService likeService;


    @PostMapping("/{object}/{id}")
    public ResponseEntity like(@PathVariable String object, @PathVariable String id) {
        if (object.equals("topic")) {
            return likeService.addLikeTopic(id);
        }
        if (object.equals("comment")) {
            return likeService.addLikeComment(id);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("no topic or Comment"));
    }

}
