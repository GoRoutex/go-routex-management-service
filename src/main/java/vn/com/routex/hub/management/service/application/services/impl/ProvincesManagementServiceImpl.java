package vn.com.routex.hub.management.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.management.service.application.command.provinces.SearchProvincesQuery;
import vn.com.routex.hub.management.service.application.command.provinces.SearchProvincesResult;
import vn.com.routex.hub.management.service.application.services.ProvincesManagementService;
import vn.com.routex.hub.management.service.domain.provinces.port.ProvincesQueryPort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvincesManagementServiceImpl implements ProvincesManagementService {

    private final ProvincesQueryPort provincesQueryPort;

    @Override
    public SearchProvincesResult searchProvinces(SearchProvincesQuery query) {
        List<SearchProvincesResult.SearchProvincesItemResult> items = provincesQueryPort.search(
                        query.getKeyword(),
                        query.getPage(),
                        query.getSize()
                ).stream()
                .map(item -> SearchProvincesResult.SearchProvincesItemResult.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .code(item.getCode())
                        .build())
                .toList();
        return SearchProvincesResult.builder()
                .data(items)
                .build();
    }
}
