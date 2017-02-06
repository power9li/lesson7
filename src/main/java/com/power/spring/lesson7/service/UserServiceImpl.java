package com.power.spring.lesson7.service;

import com.power.spring.lesson7.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by shenli on 2017/2/5.
 */
@Component
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Override
    public void createUser(String username,String password, Integer age) throws SQLException {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            LOG.info("updateLesson conn = " + connection.hashCode());
            PreparedStatement statement = connection.prepareStatement("INSERT INTO T_USERS(username,password,age) values (?,?,?)");
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setInt(3,age);
            int inserted = statement.executeUpdate();
            LOG.info("insert user count = " + inserted);
            statement.close();
            if (true) {
                throw new RuntimeException();
            }
        } finally {
            DataSourceUtils.releaseConnection(connection,dataSource);
        }
    }

    @Override
    @Transactional
    public void updateUser(User u){
        System.out.println("UserServiceImpl.updateUser");
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        template.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        template.setTimeout(300);
        template.execute(t -> {
            printUser(u.getId());
            return null;
        });
//        printUser(u);
    }

    //@Transactional
    private void printUser(final long userId) {
        System.out.println("UserServiceImpl.printUser");
        Connection connection = DataSourceUtils.getConnection(dataSource);
        User u = new User();
        try {
            LOG.info("updateLesson conn = " + connection.hashCode());
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM  T_USERS WHERE id = ?");
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                u.setId(resultSet.getLong("id"));
                u.setUsername(resultSet.getString("username"));
                u.setPassword(resultSet.getString("password"));
                u.setAge(resultSet.getInt("age"));
            }

            statement.close();
//            if (true) {
//                throw new RuntimeException();
//            }
        } catch (SQLException e){

        }
        finally {
            DataSourceUtils.releaseConnection(connection,dataSource);
        }
        LOG.info("query user = " + u);
    }

}
