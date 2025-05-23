package tr.com.oderopay.model.garantipay.request;


import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class GarantiPayInitRequestMeta {
    private String id;
    private Long timestamp;
    private String source;
    private List<GarantiPayInitRequestClientInfo> clientInfo;
}