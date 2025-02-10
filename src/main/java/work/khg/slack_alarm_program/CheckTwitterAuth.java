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
 */
@Component
public class CheckTwitterAuth {
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

    public int selectAvailableAuth(String ip) {
        return slackAlarmProgramMapper.selectAvailableAuth(ip);
    }
}

