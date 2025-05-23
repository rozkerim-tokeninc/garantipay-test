package tr.com.oderopay.model.garantipay.request;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class GarantiPayInitRequestDataReturnUrl {
    private String link;
    private String type;

}