package work.khg.slack_alarm_program;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import work.khg.common.VO.SlackWebHookUrlVO;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App {

    @Autowired
    private SlackWebHookUrlVO slackWebHookUrlVO;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);
        App app = context.getBean(App.class);
        app.run();
    }

    public void run() {
    }
}
