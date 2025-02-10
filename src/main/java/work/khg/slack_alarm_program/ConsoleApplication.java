package work.khg.slack_alarm_program;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import work.khg.common.DTO.TwitterAuthDTO;
import work.khg.common.Util.DateInputValidUtil;
import work.khg.common.VO.SlackWebHookUrlVO;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

@SpringBootApplication
@ComponentScan(basePackages = "work.khg")
@MapperScan("work.khg.common.mappers")
public class ConsoleApplication {
    private static final int TWITTER_AUTH_ERROR_STATUS_F_COUNT = 20;

    private final SlackWebHookUrlVO slackWebHookUrlVO;
    private final CheckCollectionStatus checkCollectionStatus;
    private final CheckTwitterAuth checkTwitterAuth;

    @Autowired
    public ConsoleApplication(SlackWebHookUrlVO slackWebHookUrlVO,
                              CheckCollectionStatus checkCollectionStatus, CheckTwitterAuth checkTwitterAuth) {
        this.slackWebHookUrlVO = slackWebHookUrlVO;
        this.checkCollectionStatus = checkCollectionStatus;
        this.checkTwitterAuth = checkTwitterAuth;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage:<runMode>");
            System.exit(-1);
        }
        ApplicationContext context = SpringApplication.run(ConsoleApplication.class, args);
        ConsoleApplication pr = context.getBean(ConsoleApplication.class);

        int runMode = Integer.parseInt(args[0]);
        pr.handleModeSelection(runMode);
    }

    public void handleModeSelection(int runMode) {
        switch (runMode) {
            case 1:
                sendCollectionStatusToSlack();
                break;

            case 2:
                sendTwitterStatusToSlack();
                break;

            default:
                System.out.println("Invalid runMode, runMode input 1 or 2, System exist.");
                System.exit(-1);

        }

    }

    public void sendCollectionStatusToSlack() {
        try{
            Scanner scanner = new Scanner(System.in);
            String siteType;
            do {
                System.out.println("input siteType: C(Community) or M(Media)");
                siteType = scanner.nextLine();
                if(!(siteType.equals("C") || siteType.equals("M"))) {
                    System.out.println("Site type input error, try again");
                }
            } while (!(siteType.equals("C") || siteType.equals("M")));

            DateInputValidUtil  dateInputValidUtil = new DateInputValidUtil();
            String DatePattern = "yyyyMMdd";
            String startDate = dateInputValidUtil.getDateFromUser
                    ("input startDate: yyyyMMdd", scanner, DatePattern);
            String endDate = dateInputValidUtil.getDateFromUser
                    ("input endDate: yyyyMMdd", scanner, DatePattern);

            // 수집원 단위
            List<String> activatedCrawlSiteList = this.checkCollectionStatus.returnActivatedCrawlSite(siteType);
            System.out.println("activatedCrawlSiteList: " + activatedCrawlSiteList.toString());

            List<String> collectedCrawlSiteList = this.checkCollectionStatus.returnCollectedCrawlsite(siteType, startDate, endDate);
            Set<String> check = this.checkCollectionStatus.findUncollectedSites(activatedCrawlSiteList, collectedCrawlSiteList);
            SlackApi slackApi = new SlackApi();
            String webHookUrl = this.slackWebHookUrlVO.getAlarm();
            String text = String.format("[siteType: %s, date: %s ~ %s, 수집원단위] \n %s", siteType, startDate, endDate, check.toString());
            slackApi.sendSlackText(webHookUrl, text);

            // 게시판 단위
            List<String> activatedBoardSiteList = this.checkCollectionStatus.returnActivatedBoard(siteType);
            System.out.println("activatedBoardSiteList: " + activatedBoardSiteList);

            List<String> collectedList = this.checkCollectionStatus.returnCollectedBoard(siteType, startDate, endDate);
            check = this.checkCollectionStatus.findUncollectedSites(activatedBoardSiteList, collectedList);

            StringBuffer stringBuffer = new StringBuffer();
            for (String site : check) {
                stringBuffer.append(site)
                        .append("\n");
            }
            text = String.format("[siteType: %s, date: %s ~ %s, 게시판단위] \n %s", siteType, startDate, endDate, stringBuffer.toString());
            slackApi.sendSlackText(webHookUrl, text);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendTwitterStatusToSlack() {
        checkTwitterStatusF();
        checkAvailableAuth();
    }

    /**
     * 트위터 계정의 status 값을 조회하여 블럭(F)갯수가
     * TWITTER_AUTH_ERROR_STATUS_F_COUNT 를 넘을경우 알람을 전송한다.
     */
    public void checkTwitterStatusF() {
        List<TwitterAuthDTO> twitterErrorAuthList = this.checkTwitterAuth.returnTwitterAuthStatusF();
        System.out.println(twitterErrorAuthList.size());

        if(twitterErrorAuthList.size() >= TWITTER_AUTH_ERROR_STATUS_F_COUNT) {
            String text = String.format("[TwitterAuthStatusF size: %s개, 확인필요]", twitterErrorAuthList.size());
            SlackApi slackApi = new SlackApi();
            String webHookUrl = this.slackWebHookUrlVO.getCrawl();
            slackApi.sendSlackText(webHookUrl, text);
        }
    }

    /**
     * 현재 운영서버에서 사용되는 계정들을 ip별로 사용가능한 계정을 카운트하여
     * 사용가능한 계정이 10개 미만일경우 알람을 발송한다.
     * ip는 정규식을 활용하여 제한적으로 select 한다 ( d{1,3}.d{1,3}.d{1,3}.d{1,3} )
     *
     */
    public void checkAvailableAuth() {
        List<String> ipList = this.checkTwitterAuth.selectIpList();
        String localIp = "175.125.218.16";
        ipList.remove(localIp); // local에서 test로 추가된 ip는 제외한다.

        for (String ip : ipList) {
            int availableAuthCount = this.checkTwitterAuth.selectAvailableAuth(ip);
            if (availableAuthCount < 11) {
                String text = String.format("[트위터 운영 서버: %s 계정 사용가능 갯수 10개미만 확인필요, count: %d개]", ip, availableAuthCount);
                SlackApi slackApi = new SlackApi();
                String webHookUrl = this.slackWebHookUrlVO.getCrawl();
                slackApi.sendSlackText(webHookUrl, text);
            }
        }
    }
}
