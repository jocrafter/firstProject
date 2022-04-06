package de.micromata.jonas.project.controller;

import de.micromata.jonas.project.dtos.Response;
import de.micromata.jonas.project.dtos.UserDTO;
import de.micromata.jonas.project.dtos.UserLoginDTO;
import de.micromata.jonas.project.dtos.UserRegisterDTO;
import de.micromata.jonas.project.service.AuthenticationResponse;
import de.micromata.jonas.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/user")

public class UserController {


    @Autowired
    private UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) throws Exception {

        ResponseEntity<AuthenticationResponse> authenticationResponse = userService.loginMachCheck(userLoginDTO);
        UserDTO userDTO = new UserDTO(userLoginDTO.getName());
        userDTO.setJwt(authenticationResponse.getBody().getJwt());

        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody UserRegisterDTO userRegisterDTO) throws Exception {
        boolean requestIsValid = true;
        String responseMessage = "";
        Date today = new Date();

        if (StringUtils.length(userRegisterDTO.getName()) < 4) {
            requestIsValid = false;
            responseMessage += "\n Username must contain at least 4 characters";
        } else {
            if (userService.isUserAlreadyExists(userRegisterDTO)) {
                LOG.error("User is allredy used");
                requestIsValid = false;
                responseMessage += "\n Username is already taken";
            }
        }
        if (StringUtils.isEmpty(userRegisterDTO.getPassword())) {
            requestIsValid = false;
            responseMessage += "\n No password has been entered";

        } else if (userRegisterDTO.getPassword().length() < 8) {
            requestIsValid = false;
            responseMessage += "\n Password must contain at least 8 characters";
        }
        if (!userRegisterDTO.getBirthday().before(today)) {
            requestIsValid = false;
            responseMessage += "\n Birthday is in the future, come back when you are born";
        }
        if (userRegisterDTO.getBirthday() == null) {
            requestIsValid = false;
            responseMessage += "\n No B-Day has been entered";
        }

        if (requestIsValid == false) {
            LOG.error("User not createt :{}", responseMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(responseMessage));
        }
        UserDTO userDTO;
        try {
            ResponseEntity<AuthenticationResponse> authenticationResponse = userService.userRegister(userRegisterDTO);
            userDTO = new UserDTO(userRegisterDTO.getName());
            userDTO.setJwt(authenticationResponse.getBody().getJwt());
            LOG.info("User created : {}", userRegisterDTO.getName());
        } catch (Exception e) {
            throw new Exception("Save fail");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }
}
