package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.provinces;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.provinces.port.ProvincesQueryPort;
import vn.com.routex.hub.management.service.domain.provinces.readmodel.ProvincesSearchItem;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.provinces.repository.ProvincesEntityRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JpaProvincesQueryAdapter implements ProvincesQueryPort {

    private final ProvincesEntityRepository provincesEntityRepository;

    @Override
    public List<ProvincesSearchItem> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(0, page),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Order.asc("name"))
        );

        return provincesEntityRepository.search(keyword == null ? "" : keyword.trim(), pageable)
                .map(p -> new ProvincesSearchItem(p.getId(), p.getName(), p.getCode()))
                .getContent();
    }
}
