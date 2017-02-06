package com.power.spring.lesson7.service;

import com.power.spring.lesson7.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * Created by shenli on 2017/2/5.
 */
public interface UserService {

    @Transactional
    public void createUser(String username, String password, Integer age) throws SQLException;

    public void updateUser(User user);

}
