package work.khg.common.mappers;

import org.apache.ibatis.annotations.Mapper;
import work.khg.common.DTO.ArticleSearchDTO;
import work.khg.common.DTO.TwitterAuthDTO;

import java.util.List;

@Mapper
public interface SlackAlarmProgramMapper {
    List<String> selectActivatedCrawlSiteList(String siteType);
    List<String> selectCollectedCrawlsite(ArticleSearchDTO articleSearchDTO);
    List<TwitterAuthDTO> selectStatusFTwitterId();
    List<String> selectActivatedBoardList(String siteType);
    List<String> selectCollectedBoard(ArticleSearchDTO articleSearchDTO);
    List<String> selectIpList();
    int selectAvailableAuth(String ip);
}
