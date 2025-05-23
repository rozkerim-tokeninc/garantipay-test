package tr.com.oderopay.model.garantipay.request;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class GarantiPayInitRequestData {
    private String orderId;
    private GarantiPayInitRequestDataPrice price;
    private String operation;
    private String companyName;
    private String orderInfo;
    private int timeoutPeriodInSeconds;
    private boolean bonusflashNotificationInd;
    private GarantiPayInitRequestDataReturnUrl returnUrl;
    private String notificationUrl;
    private GarantiPayInitRequestDataCustomer customer;
    private GarantiPayInitRequestDataPaymentOptions paymentOptions;

}
