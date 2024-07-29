package work.khg.slack_alarm_program;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;


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
        System.out.println(checkCollectionStatus.returnActivatedCrawlSite(siteType).size());
    }

    @Test
    public void testCheckCommunityCollection() {
        String siteType = "C";
        List<String> activatedCrawlSiteList = checkCollectionStatus.returnActivatedCrawlSite(siteType);
        System.out.println("activatedCrawlSiteList: " + activatedCrawlSiteList.toString());

        String startDate = "20240718";
        String endDate = "20240725";
        List<String> collectedCrawlSiteList = checkCollectionStatus.returnCollectedCrawlsite(siteType, startDate, endDate);
        System.out.println("collectedCrawlSiteList: " + collectedCrawlSiteList.toString());

        Set<String> check = checkCollectionStatus.CheckCommunityCollection(activatedCrawlSiteList, collectedCrawlSiteList);
        System.out.println("check: " + check.toString());

    }
}

