package com.pracsession.myFirstProject.controller;

import com.pracsession.myFirstProject.entity.User;
import com.pracsession.myFirstProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

//    @GetMapping
//    public ResponseEntity<?> getAllUsers(){
//        List<User> users = userEntryService.getAllEntries();
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }
//
//    @GetMapping("{username}")
//    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
//        User user = userEntryService.getUserByUsername(username);
//        if (user != null) {
//            return new ResponseEntity<>(user, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }


    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDb = userService.getUserByUsername(username);
           userInDb.setUsername(user.getUsername());
           userInDb.setPassword(user.getPassword());
            userService.saveUser(userInDb);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
