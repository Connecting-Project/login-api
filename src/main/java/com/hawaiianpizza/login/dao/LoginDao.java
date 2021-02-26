package com.hawaiianpizza.login.dao;

import com.hawaiianpizza.login.model.User;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface LoginDao extends JpaRepository<User, String> {
     Optional<User> findById(String id);

     Optional<User> findByIdAndPwd(String id, String pwd);
}
