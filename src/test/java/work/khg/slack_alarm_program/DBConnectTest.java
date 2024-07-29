package work.khg.slack_alarm_program;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import work.khg.common.mappers.TestMapper;

@SpringBootTest
public class DBConnectTest {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Autowired
    public TestMapper testMapper;


    @Test
    public void jdbcConnectionTest() {
            try {
                // 간단한 쿼리를 실행하여 연결 테스트
                Integer result = jdbcTemplate.queryForObject("SELECT 1 FROM DUAL", Integer.class);
                if(result == 1) {
                    System.out.println("Database connection complete");
                }
            } catch (Exception e) {
                System.err.println("Database connection test failed: " + e.getMessage());
            }
    }

    @Test
    public void mybatisConnectionTest() {
        try {
            Integer result = testMapper.test();
            if(result == 1) {
                System.out.println("mybatis connection complete");
            }
        } catch (Exception e) {
            System.err.println("mybatis connection test failed: " + e.getMessage());
        }
    }
}
