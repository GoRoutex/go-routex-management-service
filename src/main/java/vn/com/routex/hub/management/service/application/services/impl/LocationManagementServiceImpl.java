package vn.com.routex.hub.management.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.management.service.application.dto.location.SearchLocationQuery;
import vn.com.routex.hub.management.service.application.dto.location.SearchLocationResult;
import vn.com.routex.hub.management.service.application.services.LocationManagementService;
import vn.com.routex.hub.management.service.domain.location.port.LocationQueryPort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationManagementServiceImpl implements LocationManagementService {

    private final LocationQueryPort locationQueryPort;

    @Override
    public SearchLocationResult searchLocation(SearchLocationQuery query) {
        List<SearchLocationResult.SearchLocationItemResult> items = locationQueryPort.search(
                        query.getKeyword(),
                        query.getPage(),
                        query.getSize()
                ).stream()
                .map(item -> SearchLocationResult.SearchLocationItemResult.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .code(item.getCode())
                        .build())
                .toList();
        return SearchLocationResult.builder()
                .data(items)
                .build();
    }
}
