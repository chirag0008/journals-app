package com.pracsession.myFirstProject.repository;

import com.pracsession.myFirstProject.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId>{
    public User findByUsername(String username);
}
