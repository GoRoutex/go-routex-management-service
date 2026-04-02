package vn.com.routex.hub.management.service.interfaces.models.provinces;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UpdateProvinceResponse extends BaseResponse<UpdateProvinceResponse.UpdateProvinceResponseData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateProvinceResponseData {
        private Integer id;
        private String name;
        private String code;
    }
}

