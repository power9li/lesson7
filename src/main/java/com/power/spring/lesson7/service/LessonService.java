package com.power.spring.lesson7.service;

import com.power.spring.lesson7.domain.Lesson;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by shenli on 2017/2/4.
 */
@Component
public class LessonService {

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(LessonService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @Transactional(propagation= Propagation.REQUIRED)
    public void addLesson(String lessonName, int price) throws SQLException{
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            LOG.info("addLesson conn="+connection.hashCode());
            PreparedStatement statement = connection.prepareStatement("INSERT INTO T_LESSION (name,price) values (?,?)");
            statement.setString(1, lessonName);
            statement.setInt(2,price);
            int insertCount = statement.executeUpdate();
            LOG.info("insertCount:"+insertCount);
            statement.close();
        } finally {
            DataSourceUtils.releaseConnection(connection,dataSource);
        }
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    public void queryLesson() throws SQLException{
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            LOG.info("queryLesson conn = " + connection.hashCode());
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM T_LESSION");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                LOG.info("name="+resultSet.getString("name") +",price="+ resultSet.getString("price"));
            }
            statement.close();
        } finally {
            DataSourceUtils.releaseConnection(connection,dataSource);
        }
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public void updateLesson(String lessonName, int price) throws SQLException{
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            LOG.info("updateLesson conn = " + connection.hashCode());
            PreparedStatement statement = connection.prepareStatement("UPDATE T_LESSION SET price = ? WHERE NAME = ?");
            statement.setInt(1, price);
            statement.setString(2, lessonName);
            int updated = statement.executeUpdate();
            LOG.info("update price count = " + updated);
            statement.close();
            if (true) {
                throw new SQLException();
            }
        } finally {
            DataSourceUtils.releaseConnection(connection,dataSource);
        }
    }


    @Transactional
    public void updatePriceByNameUsingJdbcTemplate(String lessonName, int price) {
        final Lesson lesson = jdbcTemplate.execute((Connection con) -> {
            LOG.info("updatePriceByNameUsingJdbcTemplate.query conn=" + con);
            Lesson le = new Lesson();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM T_LESSION WHERE NAME = ?");
            ps.setString(1, lessonName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                le.setId(rs.getLong("id"));
                le.setName(rs.getString("name"));
                le.setPrice(rs.getInt("price"));
            }
            return le;
        });

        jdbcTemplate.execute((Connection conn)->{
            LOG.info("updatePriceByNameUsingJdbcTemplate.update conn=" + conn);
            PreparedStatement ps = conn.prepareStatement("UPDATE T_LESSION SET price = ? WHERE id = ?");
            ps.setInt(1, price);
            ps.setLong(2, lesson.getId());
            int updateCount = ps.executeUpdate();
            LOG.info("update Count = " + updateCount);
            return null;
        });
    }

}
