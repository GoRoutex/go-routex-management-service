package vn.com.routex.hub.management.service.interfaces.models.merchant;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.merchant.MerchantStatus;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UpdateMerchantRequest extends BaseRequest {

    @Valid
    @NotNull
    private UpdateMerchantRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateMerchantRequestData {
        @NotBlank
        private String merchantId;

        private String updatedBy;
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
