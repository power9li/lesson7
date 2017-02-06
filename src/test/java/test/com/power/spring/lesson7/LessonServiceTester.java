package test.com.power.spring.lesson7;

import com.power.spring.lesson7.config.Lesson7Config;
import com.power.spring.lesson7.service.LessonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;

/**
 * Created by shenli on 2017/2/4.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Lesson7Config.class)
public class LessonServiceTester {

    @Autowired
    private LessonService lessonService;

    @Test
    public void testQuery() throws SQLException {
        lessonService.queryLesson();
        lessonService.queryLesson();
    }

    @Test
    @Sql(value = "classpath:sql/t1.sql")
    public void testMultiThreadTransaction() throws SQLException, InterruptedException {

        Thread t1 = new Thread(){
            @Override
            public void run() {
                try {
                    lessonService.queryLesson();
                    Thread t2 = new Thread(){
                        @Override
                        public void run() {
                            try {
                                lessonService.addLesson("Scala",100);
                                lessonService.updateLesson("Scala",300);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    t2.start();
                    t2.join();
                    lessonService.queryLesson();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        t1.join();

//        lessonService.addLesson("Java基础", 400);
//        lessonService.queryLesson();

    }
}
