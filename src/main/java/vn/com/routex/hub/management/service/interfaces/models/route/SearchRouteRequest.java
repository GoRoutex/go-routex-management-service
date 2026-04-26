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

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.RegexConstant.ONLY_CHARACTER_REGEX;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.RegexConstant.ONLY_NUMBER_REGEX;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.RegexConstant.YEAR_MONTH_DATE_REGEX;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SearchRouteRequest extends BaseRequest {

    @Valid
    @NotNull
    private SearchRouteRequestData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class SearchRouteRequestData {

        @NotNull
        @NotBlank
        @Pattern(regexp = ONLY_CHARACTER_REGEX, message = "only characters are allowed for this field")
        private String origin;

        @NotNull
        @NotBlank
        @Pattern(regexp = ONLY_CHARACTER_REGEX, message = "only characters are allowed for this field")
        private String destination;

        @Pattern(regexp = YEAR_MONTH_DATE_REGEX, message = "must be in format of yyyy-MM-dd")
        private String departureDate;

        @Pattern(regexp = ONLY_NUMBER_REGEX, message = "only digits are allowed for this field")
        private String seat;

        @Pattern(regexp = ONLY_NUMBER_REGEX, message = "only digits are allowed for this field")
        private String pageSize;

        @Pattern(regexp = ONLY_NUMBER_REGEX, message = "only digits are allowed for this field")
        private String pageNumber;
    }
}
