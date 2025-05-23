package tr.com.oderopay.model.garantipay.request;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class GarantiPayInitRequestDataLoyalty {
    private String type;
    private boolean useInd;
}