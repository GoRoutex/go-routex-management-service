package vn.com.routex.hub.management.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.management.service.application.services.LocationManagementService;
import vn.com.routex.hub.management.service.domain.location.LocationRepository;
import vn.com.routex.hub.management.service.interfaces.models.location.SearchLocationResponse;

@Service
@RequiredArgsConstructor
public class LocationManagementServiceImpl implements LocationManagementService {

    private final LocationRepository locationRepository;

    @Override
    public SearchLocationResponse searchLocation(String keyword, int page, int size) {
        int safePage = Math.max(0, page);
        int safeSize = Math.min(Math.max(size, 1), 50);

        String kw = keyword == null ? "" : keyword.trim();

        Pageable pageable = PageRequest.of(
                safePage,
                safeSize,
                Sort.by(Sort.Order.asc("name"))
        );

        Page<SearchLocationResponse.SearchLocationResponseData> pageResult = locationRepository.search(kw, pageable)
                .map(l -> SearchLocationResponse.SearchLocationResponseData.builder()
                                .id(l.getId())
                                .name(l.getName())
                                .code(l.getCode())
                                .build());


        return SearchLocationResponse.builder()
                .data(pageResult.getContent())
                .build();
    }
}
