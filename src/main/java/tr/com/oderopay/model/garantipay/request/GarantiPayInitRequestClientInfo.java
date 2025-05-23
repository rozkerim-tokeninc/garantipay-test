package tr.com.oderopay.model.garantipay.request;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class GarantiPayInitRequestClientInfo {
    private String type;
    private String value;
}