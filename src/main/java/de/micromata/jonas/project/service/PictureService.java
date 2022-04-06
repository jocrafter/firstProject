package de.micromata.jonas.project.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.micromata.jonas.project.dtos.Response;
import de.micromata.jonas.project.models.Picture;
import de.micromata.jonas.project.repositorys.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Locale;


@Service
public class PictureService {

    @JsonIgnore
    private Resource fileSystemResource;
    @Autowired
    private PictureRepository pictureRepository;

    public ResponseEntity uploadPicture(MultipartFile imageFile) throws NoSuchAlgorithmException, IOException {
        String folder = "./data/pictures/";
        String type = imageFile.getContentType().split("/")[1];
        String name = imageFile.getOriginalFilename();
        byte[] data = imageFile.getBytes();
        // meta daten löschen

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(data);
        byte[] digest = md.digest();
        String hash = DatatypeConverter.printHexBinary(digest).toUpperCase(Locale.forLanguageTag("UTF-8"));

        var fileName = hash + "." + type;
        Path path = Paths.get(folder + fileName);
        if (!Files.exists(path)) {
            System.out.println("override");
            Files.write(path, data);

            BufferedImage reImage = ImageIO.read(new File(path.toString()));
            var resizeImage = resizeImage(reImage, 30, 30);
            File ops = new File(folder + "small/" + fileName);

            Files.write(ops.toPath(), data);
            ImageIO.write(resizeImage, type, ops);
            System.out.println(fileName);

            Picture picture = new Picture(hash, fileName);

            pictureRepository.save(picture);
        }


        return ResponseEntity.status(HttpStatus.OK).body(new Response(fileName));
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    public ResponseEntity<Resource> loadFile(String name) {

        String folder = "./data/pictures/";


        String imageUrl = (folder + name);
        File f = new File(imageUrl);
        fileSystemResource = new FileSystemResource(f);
        System.out.println(fileSystemResource);
        return ResponseEntity.ok()
                .body(fileSystemResource);

    }

    public String loadSmallFileByPictureName(String name) throws IOException {

        String folder = "./data/pictures/small/";
        String imageUrl = folder + name;
        File f = new File(imageUrl);
        byte[] t = Files.readAllBytes(f.toPath());
        String encoded = Base64.getEncoder().encodeToString(t);
        return encoded;
    }

    public Picture getPicture(String name) {
        Picture picture = pictureRepository.findByName(name);
        return picture;
    }


}
