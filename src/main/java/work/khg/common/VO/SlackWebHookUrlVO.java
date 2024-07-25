package work.khg.common.VO;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SlackWebHookUrlVO {
    @Value("${alarm.name.community_error.url}")
    String alarm;
}
