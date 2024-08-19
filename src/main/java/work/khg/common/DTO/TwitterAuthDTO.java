package work.khg.common.DTO;

import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class TwitterAuthDTO {
    private String email;
    private String userId;
    private String user_password;
    private String token;
    private String cookie;
    private String useUid;
    private Date createDate;
    private Timestamp useDate;
    private String status;
    private String ip;
}
