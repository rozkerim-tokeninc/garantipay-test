package tr.com.oderopay.model.garantipay.request;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class GarantiPayInitRequestDataCustomer {
    private String nationalNumber;
    private String gsmNumber;

}
