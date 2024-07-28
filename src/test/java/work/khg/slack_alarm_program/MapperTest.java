package work.khg.slack_alarm_program;

import junit.framework.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import work.khg.common.DTO.ArticleSearchDTO;
import work.khg.common.DTO.CrawlSiteDTO;
import work.khg.common.mappers.SlackAlarmProgramMapper;

import java.util.List;

@SpringBootTest
public class MapperTest {

    @Autowired
    private SlackAlarmProgramMapper slackAlarmProgramMapper;

    @Test
    public void testSelectActivatedCrawlSiteList() {
        String siteType = "C";
        List<String> list = slackAlarmProgramMapper.selectActivatedCrawlSiteList(siteType);
        System.out.println(list.get(0));
        Assert.assertEquals(!list.isEmpty(), true);
    }

    @Test
    public void testSelectCollectedCrawlsite() {
        String tableNamePreFix = "TB_ARTICLE_SEARCH_";
        String siteType = "COMM_";
        String yyMMDate = "2407";

        String tableName = tableNamePreFix + siteType + yyMMDate;
        String startDate = "20240718";
        String endDate = "20240725";

        ArticleSearchDTO articleSearchDTO = new ArticleSearchDTO();
        articleSearchDTO.setTableName(tableName);
        articleSearchDTO.setStartDate(startDate);
        articleSearchDTO.setEndDate(endDate);

       List<String> list =  slackAlarmProgramMapper.selectCollectedCrawlsite(articleSearchDTO);
       Assert.assertEquals(!list.isEmpty(), true);
    }
}
