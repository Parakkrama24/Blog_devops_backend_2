package com.devops.Blog.service;

import com.devops.Blog.model.user;

public interface userService {

    public user findUserByEmail(String email) throws Exception;
}
