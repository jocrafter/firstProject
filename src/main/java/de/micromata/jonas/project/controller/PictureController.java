package de.micromata.jonas.project.controller;

import de.micromata.jonas.project.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/pictures")
public class PictureController {
    @Autowired
    private PictureService pictureService;

    //to Igrnore the security conf


    @PostMapping()
    public ResponseEntity newPicture(@RequestParam("imageFile") MultipartFile imageFile) throws Exception {
        System.out.println(imageFile);
        return pictureService.uploadPicture(imageFile);

    }

    @GetMapping("/")
    public @ResponseBody
    ResponseEntity<Resource> getFile(@RequestParam("name") String name) throws IOException {

        return pictureService.loadFile(name);

 /*
        InputStream i = FileUtils.openInputStream(f);
        var b = IOUtils.toByteArray(i);
        byte[] encode = java.util.Base64.getEncoder().encode(b);
        model.addAttribute("image", new String(encode, "UTF-8"));

         System.out.println("File exist " + f.exists());
         System.out.println(f.getAbsolutePath());

         OutputStream outputStream = httpResponse.getOutputStream();

         FileUtils.copyFile(f, outputStream);
         outputStream.flush();
         outputStream.close();
         System.out.println(model);
         */


    }

}

