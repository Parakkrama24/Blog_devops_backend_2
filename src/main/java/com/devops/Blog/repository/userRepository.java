package com.devops.Blog.repository;

import com.devops.Blog.model.user;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<user, Long> {

    public user findUserByEmail(String userName);
}
