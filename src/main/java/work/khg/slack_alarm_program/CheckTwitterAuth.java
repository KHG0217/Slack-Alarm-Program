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

    @Autowired
    private SlackAlarmProgramMapper slackAlarmProgramMapper;

    /**
     * Twitter Status 값이 F인 계정을 반환한다.
     * @return List<TwitterAuthDTO>
     * @See 트위터 수집기에서 계정을 수집할때, 트위터 측에서 계정을 일시잠금하거나
     * 그외 계정을 사용할 수 없는 상황일때 F로 변경한다.
     */
    public List<TwitterAuthDTO> returnTwitterAuthStatusF() {
        return slackAlarmProgramMapper.selectStatusFTwitterId();
    }
}
