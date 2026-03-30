package vn.com.routex.hub.management.service.interfaces.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import vn.com.routex.hub.management.service.application.command.provinces.SearchProvincesQuery;
import vn.com.routex.hub.management.service.application.command.provinces.SearchProvincesResult;
import vn.com.routex.hub.management.service.application.services.ProvincesManagementService;
import vn.com.routex.hub.management.service.interfaces.models.provinces.SearchProvincesResponse;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.PROVINCES_SERVICE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.SEARCH_PATH;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH)
@PreAuthorize("hasAuthority('provinces:management') or hasRole('ADMIN')")
public class ProvincesManagementController {


    private final ProvincesManagementService provincesManagementService;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }

    @GetMapping(PROVINCES_SERVICE + SEARCH_PATH)
    public ResponseEntity<SearchProvincesResponse> searchProvinces(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size) {
        SearchProvincesResult result = provincesManagementService.searchProvinces(SearchProvincesQuery.builder()
                .keyword(keyword)
                .page(page)
                .size(size)
                .build());

        SearchProvincesResponse response = SearchProvincesResponse.builder()
                .data(result.getData().stream()
                        .map(item -> SearchProvincesResponse.SearchProvincesResponseData.builder()
                                .id(item.getId())
                                .name(item.getName())
                                .code(item.getCode())
                                .build())
                        .toList())
                .build();

        return ResponseEntity.ok(response);
    }
}
