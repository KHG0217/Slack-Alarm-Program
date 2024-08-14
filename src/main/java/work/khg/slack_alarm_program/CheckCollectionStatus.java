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
    private final String TABLE_NAME_PREFIX = "TB_ARTICLE_SEARCH_";

    @Autowired
    private SlackAlarmProgramMapper slackAlarmProgramMapper;

    /**
     *  siteType을 인자로 받아, 활성화 되어있는 수집원들을 리스트로 반환한다.
     * @param siteType
     * @return activatedCrawlSiteList
     */
    public List<String> returnActivatedCrawlSite(String siteType) {
        List<String> activatedCrawlSiteList = new ArrayList<String>();
        activatedCrawlSiteList = slackAlarmProgramMapper.selectActivatedCrawlSiteList(siteType);
        return activatedCrawlSiteList;
    }

    public List<String> returnCollectedCrawlsite(String siteType, String startDate, String endDate) {

        List<String> collectedCrawlSiteList = new ArrayList<String>();

        String tableSiteType = NameUtil.convertSiteTypeToTableSiteType(siteType);
        String startingTableName = startDate.substring(2,6);
        String endingTableName = endDate.substring(2,6);

        String tabelName = TABLE_NAME_PREFIX + tableSiteType + startingTableName;
        ArticleSearchDTO articleSearchDTO = new ArticleSearchDTO();
        articleSearchDTO.setTableName(tabelName);
        articleSearchDTO.setSiteType(siteType);
        articleSearchDTO.setStartDate(startDate);
        articleSearchDTO.setEndDate(endDate);

        if(startingTableName.equals(endingTableName)) {
            collectedCrawlSiteList = slackAlarmProgramMapper.selectCollectedCrawlsite(articleSearchDTO);
        }else {
            String tabelNamePre = TABLE_NAME_PREFIX + tableSiteType + startingTableName;
            ArticleSearchDTO articleSearchDTOPre = new ArticleSearchDTO();
            articleSearchDTOPre.setTableName(tabelNamePre);
            articleSearchDTOPre.setSiteType(siteType);
            articleSearchDTOPre.setStartDate(startDate);
            articleSearchDTOPre.setEndDate(endDate);
            List<String> collectedCrawlSitePreList = slackAlarmProgramMapper.selectCollectedCrawlsite(articleSearchDTOPre);

            tabelName = TABLE_NAME_PREFIX + tableSiteType + endingTableName;
            articleSearchDTO.setTableName(tabelName);

            collectedCrawlSiteList = slackAlarmProgramMapper.selectCollectedCrawlsite(articleSearchDTO);

            // Set을 사용하여 중복 제거
            Set<String> set = new HashSet<>(collectedCrawlSitePreList);
            set.addAll(collectedCrawlSiteList);

            collectedCrawlSiteList = new ArrayList<>(set);

        }

        return collectedCrawlSiteList;
    }

    public  Set<String> CheckCommunityCollection(List<String> activatedCrawlSiteList,
                                         List<String> collectedCrawlSiteList) {
        Set<String> activatedCrawlSiteSet = new HashSet<String>(activatedCrawlSiteList);
        Set<String> collectedCrawlSiteSet = new HashSet<String>(collectedCrawlSiteList);
        Set<String> CheckCommunitySiteSet = new HashSet<String>();

        for(String activatedCrawlSite : activatedCrawlSiteList) {
            if (!collectedCrawlSiteSet.contains(activatedCrawlSite)) {
                CheckCommunitySiteSet.add(activatedCrawlSite);
            }
        }
        return CheckCommunitySiteSet;
    }


}
