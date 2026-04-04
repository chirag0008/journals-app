package com.pracsession.myFirstProject.controller;

import com.pracsession.myFirstProject.entity.User;
import com.pracsession.myFirstProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers(){
        List<User> users = userService.getAllEntries();
        if(users!=null && !users.isEmpty()){
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-new-admin")
    public ResponseEntity<?> createUser(@RequestBody User user){
        userService.saveAdmin(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

}
