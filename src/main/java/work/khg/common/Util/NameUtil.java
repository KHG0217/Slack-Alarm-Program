package work.khg.common.Util;

public class NameUtil {

    /**
     * 매채별 타입을 게시물 테이블 이름으로 변환한다.
     * @param siteType // M,C,R,T,I,V
     * @return convertedSitetype // MEDIA_,COMM_,RSS_,TWITTER_,INSTA_,VIDEO_
     */
    public static String convertSiteTypeToTableSiteType(String siteType) {
        String convertedSitetype = null;

        switch (siteType) {
            case "M":
                convertedSitetype = "MEDIA_";
                break;
            case "C":
                convertedSitetype = "COMM_";
                break;
            case "R":
                convertedSitetype = "RSS_";
                break;
            case "T":
                convertedSitetype = "TWITTER_";
                break;
            case "I":
                convertedSitetype = "INSTA_";
                break;
            case "V":
                convertedSitetype = "VIDEO_";
                break;
        }
        return convertedSitetype;
    }

    public String subStrDateToTableDateName(String date) {
        String convertedDateName = null;

        try {
            convertedDateName = date.substring(2,5);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return convertedDateName;
    }

    public static void main(String[] args) {
        System.out.println(convertSiteTypeToTableSiteType("M"));
        System.out.println("20240718".substring(2,6));
    }
}
