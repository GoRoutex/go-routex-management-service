package vn.com.routex.hub.user.service.application.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.application.services.LocationManagementService;
import vn.com.routex.hub.user.service.interfaces.models.location.SearchLocationResponse;

@Component
@RequiredArgsConstructor
public class LocationManagementFacadeImpl implements LocationManagementFacade{

    private final LocationManagementService locationManagementService;

    @Override
    public ResponseEntity<SearchLocationResponse> searchLocation(String keyword, int page, int size) {
        return new ResponseEntity<>(locationManagementService.searchLocation(keyword, page, size), HttpStatus.OK);
    }
}
