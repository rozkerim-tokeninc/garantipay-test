package tr.com.oderopay.model.garantipay.request;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class GarantiPayInitRequestDataPrice {
    private String amount;
    private Integer currency;
}