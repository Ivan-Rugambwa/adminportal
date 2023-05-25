package kim.almroth.javazeebee.message.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class StartSingleRequest {
    private String businessUuid;
    private String forDate;
}
