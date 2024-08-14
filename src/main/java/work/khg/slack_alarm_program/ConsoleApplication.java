package work.khg.slack_alarm_program;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import work.khg.common.DTO.CrawlSiteDTO;
import work.khg.common.VO.SlackWebHookUrlVO;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

@SpringBootApplication
@ComponentScan(basePackages = "work.khg")
@MapperScan("work.khg.common.mappers")
public class ConsoleApplication {

    @Autowired
    private Environment environment;

    @Autowired
    private SlackWebHookUrlVO slackWebHookUrlVO;

    @Autowired
    CheckCollectionStatus checkCollectionStatus;


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
                checkTwitterAuth();
                break;

            default:
                System.out.println("Invalid runMode, runMode input 1 or 2, System exist.");
                System.exit(-1);

        }

    }

    public void sendCollectionStatusToSlack() {
        System.out.println("input siteType: C(Community) or M(Media)");
        Scanner scanner = new Scanner(System.in);
        String siteType = scanner.nextLine();

        if(!(siteType.equals("C") || siteType.equals("M"))) {
            System.out.println("Site type input error, try again");
            sendCollectionStatusToSlack();
        }

        System.out.println("input startDate: yyyyMMdd");
        scanner = new Scanner(System.in);
        String startDate = scanner.nextLine();

        System.out.println("input endDate: yyyyMMdd");
        scanner = new Scanner(System.in);
        String endDate = scanner.nextLine();
        List<String> activatedCrawlSiteList = checkCollectionStatus.returnActivatedCrawlSite(siteType);
        System.out.println("activatedCrawlSiteList: " + activatedCrawlSiteList.toString());

        List<String> collectedCrawlSiteList = checkCollectionStatus.returnCollectedCrawlsite(siteType, startDate, endDate);

        Set<String> check = checkCollectionStatus.CheckCommunityCollection(activatedCrawlSiteList, collectedCrawlSiteList);

        SlackApi slackApi = new SlackApi();
        String webHookUrl = slackWebHookUrlVO.getAlarm();
        String text = String.format("[siteType: %s, date: %s ~ %s] \n %s", siteType, startDate, endDate, check.toString());
        slackApi.sendSlackText(webHookUrl, text);
    }

    public void checkTwitterAuth() {
    }
//    public CommandLineRunner demo(MyService myService) {
//        return (args) -> {
//           myService.performDataBaseOperations();
//        } ;
//    }

//    @Override
//    public void run(String... args) throws Exception {
////        myService.performDataBaseOperations();
//    }
}
