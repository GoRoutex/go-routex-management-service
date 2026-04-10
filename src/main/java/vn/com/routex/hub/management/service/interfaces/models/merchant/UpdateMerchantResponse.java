package vn.com.routex.hub.management.service.interfaces.models.merchant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.merchant.MerchantStatus;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UpdateMerchantResponse extends BaseResponse<UpdateMerchantResponse.UpdateMerchantResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateMerchantResponseData {
        private String id;
        private String code;
        private String name;
        private String taxCode;
        private String phone;
        private String email;
        private String address;
        private String representativeName;
        private BigDecimal commissionRate;
        private MerchantStatus status;
    }
}
