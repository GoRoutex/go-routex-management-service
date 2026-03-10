package vn.com.routex.hub.management.service.interfaces.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.hub.management.service.application.facade.LocationManagementFacade;
import vn.com.routex.hub.management.service.interfaces.models.location.SearchLocationResponse;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.LOCATION_SERVICE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.SEARCH_PATH;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH)
@PreAuthorize("hasAuthority('location:management') and hasRole('ADMIN')")
public class LocationManagementController {


    private final LocationManagementFacade locationManagementFacade;

    @GetMapping(LOCATION_SERVICE + SEARCH_PATH)
    public ResponseEntity<SearchLocationResponse> searchLocation(
            @RequestParam String keyword,
            @RequestParam int page,
            @RequestParam int size) {
        return locationManagementFacade.searchLocation(keyword, page, size);
    }
}
