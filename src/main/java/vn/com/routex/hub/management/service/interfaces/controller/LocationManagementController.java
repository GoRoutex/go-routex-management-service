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
import vn.com.routex.hub.management.service.application.command.location.SearchLocationQuery;
import vn.com.routex.hub.management.service.application.command.location.SearchLocationResult;
import vn.com.routex.hub.management.service.application.services.LocationManagementService;
import vn.com.routex.hub.management.service.interfaces.models.location.SearchLocationResponse;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.LOCATION_SERVICE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.SEARCH_PATH;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH)
@PreAuthorize("hasAuthority('location:management') or hasRole('ADMIN')")
public class LocationManagementController {


    private final LocationManagementService locationManagementService;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }

    @GetMapping(LOCATION_SERVICE + SEARCH_PATH)
    public ResponseEntity<SearchLocationResponse> searchLocation(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size) {
        SearchLocationResult result = locationManagementService.searchLocation(SearchLocationQuery.builder()
                .keyword(keyword)
                .page(page)
                .size(size)
                .build());

        SearchLocationResponse response = SearchLocationResponse.builder()
                .data(result.getData().stream()
                        .map(item -> SearchLocationResponse.SearchLocationResponseData.builder()
                                .id(item.getId())
                                .name(item.getName())
                                .code(item.getCode())
                                .build())
                        .toList())
                .build();

        return ResponseEntity.ok(response);
    }
}
