package work.khg.slack_alarm_program;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import work.khg.common.DTO.TwitterAuthDTO;
import work.khg.common.mappers.SlackAlarmProgramMapper;

import java.util.List;

/**
 * @author hyukg
 * @date  2024-08-14
 * @see 트위터 계정의 상태를 체크하는 함수를 만들어 반환한다.
 *
 */
@Component
public class CheckTwitterAuth {
    private static final int TWITTER_AUTH_ERROR_STATUS_F_COUNT = 20;
    private final SlackAlarmProgramMapper slackAlarmProgramMapper;

    @Autowired
    public CheckTwitterAuth(SlackAlarmProgramMapper slackAlarmProgramMapper) {
        this.slackAlarmProgramMapper = slackAlarmProgramMapper;
    }

    /**
     * Twitter Status 값이 F인 계정을 반환한다.
     * @return List<TwitterAuthDTO>
     * @See 트위터 수집기에서 계정을 수집할때, 트위터 측에서 계정을 일시잠금하거나
     * 그외 계정을 사용할 수 없는 상황일때 F로 변경한다.
     */
    public List<TwitterAuthDTO> returnTwitterAuthStatusF() {
        return slackAlarmProgramMapper.selectStatusFTwitterId();
    }

    /**
     * Twitter 수집 운영에 사용되는 IP를 반환한다.
     * 정규식을 활용하여 제한적으로 select한다.
     *
     * @return List<String>
     */
    public List<String> selectIpList() {
        return slackAlarmProgramMapper.selectIpList();
    }

    /**
    public int selectAvailableAuth(String ip) {
        return slackAlarmProgramMapper.selectAvailableAuth(ip);
    }

    /**
     * 트위터 계정의 status 값을 조회하여 블럭(F)갯수가
     * TWITTER_AUTH_ERROR_STATUS_F_COUNT 를 넘을경우 알람을 전송한다.
     */
    public String checkTwitterStatusF() {
        List<TwitterAuthDTO> twitterErrorAuthList = slackAlarmProgramMapper.selectStatusFTwitterId();
        System.out.println(twitterErrorAuthList.size());

        String text = null;
        if(twitterErrorAuthList.size() >= TWITTER_AUTH_ERROR_STATUS_F_COUNT) {
            text = String.format("[TwitterAuthStatusF size: %s개, 확인필요]", twitterErrorAuthList.size());
        }
        return text;
    }

    /**
     * 현재 운영서버에서 사용되는 계정들을 ip별로 사용가능한 계정을 카운트하여
     * 사용가능한 계정이 10개 미만일경우 알람을 발송한다.
     * ip는 정규식을 활용하여 제한적으로 select 한다 ( d{1,3}.d{1,3}.d{1,3}.d{1,3} )
     *
     */
    public String checkAvailableAuth(String ip) {
        int availableAuthCount = slackAlarmProgramMapper.selectAvailableAuth(ip);
        String text = null;
        if (availableAuthCount < 11) {
            text = String.format("[트위터 운영 서버: %s 계정 사용가능 갯수 10개미만 확인필요, count: %d개]", ip, availableAuthCount);
        }
        return text;
    }

    public String checkAvailableXctXidToken(String ip) {
            int availableAuthCount = slackAlarmProgramMapper.selectAvailableToken(ip);
           String text = null;
            if (availableAuthCount < 50) {
                text = String.format("[트위터 운영 서버: %s X-clientId 토큰 사용가능 갯수 50개미만 확인필요, count: %d개]", ip, availableAuthCount);
            }
        return text;
    }

}

