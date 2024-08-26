package work.khg.common.mappers;

import org.apache.ibatis.annotations.Mapper;
import work.khg.common.DTO.ArticleSearchDTO;
import work.khg.common.DTO.CrawlSiteDTO;
import work.khg.common.DTO.TwitterAuthDTO;

import java.util.List;

@Mapper
public interface SlackAlarmProgramMapper {
    List<String> selectActivatedCrawlSiteList(String siteType);
    List<String> selectCollectedCrawlsite(ArticleSearchDTO articleSearchDTOeDto);
    List<TwitterAuthDTO> selectStatusFTwitterId();
    List<String> selectActivatedBoardList(String siteType);
    List<String> selectCollectedBoard(ArticleSearchDTO articleSearchDTOeDto);
}
