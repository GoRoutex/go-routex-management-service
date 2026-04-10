package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.provinces;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.provinces.port.ProvincesQueryPort;
import vn.com.routex.hub.management.service.domain.provinces.readmodel.ProvincesFetchView;
import vn.com.routex.hub.management.service.domain.provinces.readmodel.ProvincesSearchItem;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.provinces.entity.ProvincesEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.provinces.repository.ProvincesEntityRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProvincesQueryAdapter implements ProvincesQueryPort {

    private final ProvincesEntityRepository provincesEntityRepository;

    @Override
    public List<ProvincesSearchItem> search(String merchantId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(0, page),
                Math.min(Math.max(size, 1), 50),
                Sort.by(Sort.Order.asc("name"))
        );

        return provincesEntityRepository.searchByMerchantId(merchantId, keyword == null ? "" : keyword.trim(), pageable)
                .map(p -> {
                    ProvincesSearchItem item = new ProvincesSearchItem();
                    item.setId(p.getId());
                    item.setName(p.getName());
                    item.setCode(p.getCode());
                    return item;
                })
                .getContent();
    }

    @Override
    public PagedResult<ProvincesFetchView> fetchRoutes(String merchantId, int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ProvincesEntity> page = provincesEntityRepository.fetchByMerchantId(merchantId, pageable);

        List<ProvincesFetchView> items = page.getContent().stream()
                .map(p -> {
                    ProvincesFetchView view = new ProvincesFetchView();
                    view.setId(p.getId());
                    view.setName(p.getName());
                    view.setCode(p.getCode());
                    return view;
                })
                .toList();

        return PagedResult.<ProvincesFetchView>builder()
                .items(items)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
