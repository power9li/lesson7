package test.com.power.spring.lesson7;

import com.power.spring.lesson7.domain.User;
import com.power.spring.lesson7.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by shenli on 2017/2/6.
 */
public class AspectJTransactionDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.power.spring.lesson7");
        UserService userService = context.getBean(UserService.class);
        User u = new User();
        u.setId(1L);
        u.setAge(30);
        u.setUsername("JackChen");
        u.setPassword("123");
        userService.updateUser(u);
    }
}
