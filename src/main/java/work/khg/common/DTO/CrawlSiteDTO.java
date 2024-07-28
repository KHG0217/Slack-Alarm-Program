package work.khg.common.DTO;

import lombok.Data;

@Data
public class CrawlSiteDTO {
    private String siteIdOld;
    private String siteName;
    private char siteType;
    private Long following;
    private Long follower;
    private Long listed;
    private Long tweet;
    private String picture;
    private String bio;
    private String web;
    private String location;
    private char status;
    private String url;
    private Long priority;
    private String createUserDate;
    private String createUserId;
    private String updateUserDate;
    private String updateUserId;
    private String screenName;
    private String siteId;
    private String siteCode;
    private String siteSubCate;
    private String lastUpdatedDate;
    private Long hitCountFromUpdated;
    private Long hitCountTotal;
    private char isValidLang;
    private String siteCategory;
    private String publisherType;
}
