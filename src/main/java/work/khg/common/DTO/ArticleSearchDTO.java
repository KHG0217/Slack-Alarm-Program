package work.khg.common.DTO;

import lombok.Data;

@Data
public class ArticleSearchDTO {
    private Long seq;
    private Long articleId;
    private Long siteIdOld;
    private String writerId;
    private String crawlDate;
    private String title;
    private String body;
    private Long rt;
    private String replyId;
    private String replyWriterId;
    private char re;
    private String address;
    private Long lat;
    private Long lng;
    private String createDate;
    private String siteType;
    private String viaUrl;
    private char addressStatus;
    private char mention;
    private Long articleIdOld;
    private String url;
    private String reputationType;
    private String siteSubType;
    private String contentId;
    private String address2;
    private String siteId;
    private String siteCode;
    private String writerName;
    private char collectedBy;
    private Long rtCount;
    private Long followerCount;
    private String siteName;
    private String picture;
    private String screenName;
    private String siteCategory;
    private Long hitCount;
    private Long commentCount;
    private Long likeCount;
    private String tableName;
    private String startDate;
    private String endDate;
}
