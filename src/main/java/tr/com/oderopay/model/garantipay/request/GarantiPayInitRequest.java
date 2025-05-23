package tr.com.oderopay.model.garantipay.request;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class GarantiPayInitRequest {
    private GarantiPayInitRequestMeta meta;
    private GarantiPayInitRequestData data;
}