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
    private static final int TWITTER_AUTH_ERROR_THRESHOLD = 20;

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

            List<String> activatedCrawlSiteList = this.checkCollectionStatus.returnActivatedCrawlSite(siteType);
            System.out.println("activatedCrawlSiteList: " + activatedCrawlSiteList.toString());

            List<String> collectedCrawlSiteList = this.checkCollectionStatus.returnCollectedCrawlsite(siteType, startDate, endDate);
            Set<String> check = this.checkCollectionStatus.findUncollectedSites(activatedCrawlSiteList, collectedCrawlSiteList);

            SlackApi slackApi = new SlackApi();
            String webHookUrl = this.slackWebHookUrlVO.getAlarm();
            String text = String.format("[siteType: %s, date: %s ~ %s] \n %s", siteType, startDate, endDate, check.toString());
            slackApi.sendSlackText(webHookUrl, text);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendTwitterStatusToSlack() {
        List<TwitterAuthDTO> twitterErrorAuthList = this.checkTwitterAuth.returnTwitterAuthStatusF();
        System.out.println(twitterErrorAuthList.size());

        if(twitterErrorAuthList.size() >= TWITTER_AUTH_ERROR_THRESHOLD) {
            String text = String.format("[TwitterAuthStatusF size: %s개, 확인필요]", twitterErrorAuthList.size());
            SlackApi slackApi = new SlackApi();
            String webHookUrl = this.slackWebHookUrlVO.getCrawl();
            slackApi.sendSlackText(webHookUrl, text);
        }
    }
}
