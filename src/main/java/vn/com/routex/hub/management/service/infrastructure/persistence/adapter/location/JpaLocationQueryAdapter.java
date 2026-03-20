package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.location;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.location.port.LocationQueryPort;
import vn.com.routex.hub.management.service.domain.location.readmodel.LocationSearchItem;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.location.repository.LocationJpaRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JpaLocationQueryAdapter implements LocationQueryPort {

    private final LocationJpaRepository locationJpaRepository;

    @Override
    public List<LocationSearchItem> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(0, page),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Order.asc("name"))
        );

        return locationJpaRepository.search(keyword == null ? "" : keyword.trim(), pageable)
                .map(location -> new LocationSearchItem(location.getId(), location.getName(), location.getCode()))
                .getContent();
    }
}
