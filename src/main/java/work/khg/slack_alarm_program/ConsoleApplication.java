package work.khg.slack_alarm_program;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import work.khg.common.VO.SlackWebHookUrlVO;

@SpringBootApplication
@ComponentScan(basePackages = "work.khg")
@MapperScan("work.khg.common.mappers")
public class ConsoleApplication {

    @Autowired
    private Environment environment;

    @Autowired
    private SlackWebHookUrlVO slackWebHookUrlVO;

    public static void main(String[] args) {
        SpringApplication.run(ConsoleApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(JdbcTemplate jdbcTemplate) {
        return (args) -> {

        } ;
    }

//    public CommandLineRunner demo(MyService myService) {
//        return (args) -> {
//           myService.performDataBaseOperations();
//        } ;
//    }

//    @Override
//    public void run(String... args) throws Exception {
////        myService.performDataBaseOperations();
//    }
}
