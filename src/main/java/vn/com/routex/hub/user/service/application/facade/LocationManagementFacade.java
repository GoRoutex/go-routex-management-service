package vn.com.routex.hub.user.service.application.facade;

import org.springframework.http.ResponseEntity;
import vn.com.routex.hub.user.service.interfaces.models.location.SearchLocationResponse;

public interface LocationManagementFacade {
    ResponseEntity<SearchLocationResponse> searchLocation(String keyword, int page, int size);
}
