package com.devops.Blog.service;

import com.devops.Blog.model.user;
import com.devops.Blog.repository.userRepository;
import com.devops.Blog.service.userService;
import org.springframework.stereotype.Service;

@Service
public class userServiceImplementation implements userService{

    private userRepository UserRepository;
    @Override
    public user findUserByEmail(String userName) throws Exception {
        user User= UserRepository.findUserByEmail(userName);
        if(User!=null){
            return User;
        }
        throw new Exception("User doesn't exist");
    }
}
