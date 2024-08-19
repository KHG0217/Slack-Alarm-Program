package work.khg.slack_alarm_program;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import work.khg.common.DTO.ArticleSearchDTO;
import work.khg.common.DTO.CrawlSiteDTO;
import work.khg.common.Util.NameUtil;
import work.khg.common.mappers.SlackAlarmProgramMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author hyukg
 * @date  2024-07-26
 * @see 매채별 수집상태를 확인하는 함수를 만들어 반환한다.
 */
@Component
public class CheckCollectionStatus {
    private final SlackAlarmProgramMapper slackAlarmProgramMapper;

    @Autowired
    public CheckCollectionStatus(SlackAlarmProgramMapper slackAlarmProgramMapper) {
        this.slackAlarmProgramMapper = slackAlarmProgramMapper;
    }

    /**
     *  siteType을 인자로 받아, 활성화 되어있는 수집원들을 리스트로 반환한다.
     * @param siteType
     * @return activatedCrawlSiteList
     */
    public List<String> returnActivatedCrawlSite(String siteType) {
        return slackAlarmProgramMapper.selectActivatedCrawlSiteList(siteType);
    }

    public List<String> returnCollectedCrawlsite(String siteType, String startDate, String endDate) {
        List<String> collectedCrawlSiteList = new ArrayList<String>();
        try{
            String tableSiteType = NameUtil.convertSiteTypeToTableSiteType(siteType);
            String startingTableName = startDate.substring(2,6);
            String endingTableName = endDate.substring(2,6);

            ArticleSearchDTO articleSearchDTO = new ArticleSearchDTO();
            articleSearchDTO.setSiteType(siteType);
            articleSearchDTO.setStartDate(startDate);
            articleSearchDTO.setEndDate(endDate);

            String TABLE_NAME_PREFIX = "TB_ARTICLE_SEARCH_";
            if(startingTableName.equals(endingTableName)) {
                String tableName = TABLE_NAME_PREFIX + tableSiteType + startingTableName;
                articleSearchDTO.setTableName(tableName);
                collectedCrawlSiteList = slackAlarmProgramMapper.selectCollectedCrawlsite(articleSearchDTO);
            }else {
                String tableNamePre = TABLE_NAME_PREFIX + tableSiteType + startingTableName;
                articleSearchDTO.setTableName(tableNamePre);

                List<String> collectedCrawlSitePreList = slackAlarmProgramMapper.selectCollectedCrawlsite(articleSearchDTO);
                String tableNamePost = TABLE_NAME_PREFIX + tableSiteType + endingTableName;
                articleSearchDTO.setTableName(tableNamePost);

                collectedCrawlSiteList = slackAlarmProgramMapper.selectCollectedCrawlsite(articleSearchDTO);

                // Set을 사용하여 중복 제거
                Set<String> set = new HashSet<>(collectedCrawlSitePreList);
                set.addAll(collectedCrawlSiteList);
                collectedCrawlSiteList = new ArrayList<>(set);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return collectedCrawlSiteList;
    }

    public  Set<String> findUncollectedSites(List<String> activatedCrawlSiteList,
                                         List<String> collectedCrawlSiteList) {
        Set<String> activatedCrawlSiteSet = new HashSet<>(activatedCrawlSiteList);
        Set<String> collectedCrawlSiteSet = new HashSet<>(collectedCrawlSiteList);
        activatedCrawlSiteSet.removeAll(collectedCrawlSiteSet);
        return activatedCrawlSiteSet;
    }


}
