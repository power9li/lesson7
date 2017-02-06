package test.com.power.spring.lesson7;

import com.power.spring.lesson7.service.LessonService;
import com.power.spring.lesson7.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

/**
 * Created by shenli on 2017/2/4.
 */
public class MainTest {
    public static void main(String[] args) throws SQLException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.power.spring.lesson7");
//        LessonService lessonService = context.getBean(LessonService.class);
//        testMethodTransaction(lessonService);

        UserService userService = context.getBean(UserService.class);
        testInterfaceTransaction(userService);
    }

    public static void testMethodTransaction(LessonService lessonService) throws SQLException{
        lessonService.queryLesson();
        lessonService.queryLesson();

        lessonService.updatePriceByNameUsingJdbcTemplate("Java基础", 5000);
    }

    public static void testInterfaceTransaction(UserService userService) throws SQLException{
        userService.createUser("李四", "l4pass01", 24);
    }
}
