package work.khg.slack_alarm_program;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class AppTest {

    @Autowired
    CheckCollectionStatus checkCollectionStatus;

    @Test
    public void testCheckCoolectionStatus() {
        String siteType = "C";
        String startDate = "20240718";
        String endDate = "20240725";

        System.out.println(checkCollectionStatus.returnCollectedCrawlsite(siteType,startDate,endDate).size());
    }

    @Test
    public void testReturnActivatedCrawlSite() {
        String siteType = "C";
        System.out.println(checkCollectionStatus.returnActivatedCrawlSite(siteType));
    }
}

