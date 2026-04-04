package com.pracsession.myFirstProject.controller;

import com.pracsession.myFirstProject.entity.User;
import com.pracsession.myFirstProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    UserService userService;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "ok";
    }

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user){
        userService.saveNewUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
