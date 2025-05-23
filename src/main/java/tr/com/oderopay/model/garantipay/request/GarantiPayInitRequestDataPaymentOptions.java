package tr.com.oderopay.model.garantipay.request;


import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class GarantiPayInitRequestDataPaymentOptions {
    private boolean threeDSecureCheck;
    private boolean cvcRequired;
    private String paymentOptionsUrl;
    private boolean addCampaignInstallment;
    private boolean showOnlyInstallments;
    private boolean installmentOnlyForCommercialCard;
    private List<GarantiPayInitRequestDataLoyalty> loyalties;

}