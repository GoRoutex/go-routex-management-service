package vn.com.routex.hub.management.service.interfaces.models.route;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.interfaces.models.base.BaseRequest;

import java.util.List;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApplicationConstant.OFFSET_DATE_TIME_REGEX;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateRouteRequest extends BaseRequest {

    @Valid
    @NotNull
    private CreateRouteRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class CreateRouteRequestData {

        @NotBlank
        @NotNull
        private String creator;

        @NotNull
        @NotBlank
        private String pickupBranch;

        @NotBlank
        @NotNull
        private String origin;

        @NotBlank
        @NotNull
        private String destination;

        @NotBlank
        @NotNull
        @Pattern(regexp = OFFSET_DATE_TIME_REGEX, message= "must be in format of yyyy-MM-ddTHH:mm:ss+timezone e.g. 2026-03-03T14:30:00+07:00")
        private String plannedStartTime;

        @NotBlank
        @NotNull
        @Pattern(regexp = OFFSET_DATE_TIME_REGEX, message= "must be in format of yyyy-MM-ddTHH:mm:ss+timezone e.g. 2026-03-03T14:30:00+07:00")
        private String plannedEndTime;

        private List<OperationPoints> operationPoints;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class OperationPoints {
        private String operationOrder;
        private String plannedArrivalTime;
        private String plannedDepartureTime;
        private String note;
    }
}
