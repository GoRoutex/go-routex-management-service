package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.merchant;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.merchant.model.Merchant;
import vn.com.routex.hub.management.service.domain.merchant.port.MerchantRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.merchant.entity.MerchantEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.merchant.repository.MerchantEntityRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MerchantRepositoryAdapter implements MerchantRepositoryPort {

    private final MerchantEntityRepository merchantEntityRepository;

    @Override
    public Merchant save(Merchant merchant) {
        MerchantEntity saved = merchantEntityRepository.save(MerchantPersistenceMapper.toEntity(merchant));
        return MerchantPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Merchant> findById(String id) {
        return merchantEntityRepository.findById(id)
                .map(MerchantPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByCode(String code) {
        return merchantEntityRepository.existsByCode(code);
    }

    @Override
    public String generateMerchantCode() {
        return merchantEntityRepository.generateMerchantcode();
    }

    @Override
    public PagedResult<Merchant> fetch(int pageNumber, int pageSize) {
        Page<MerchantEntity> page = merchantEntityRepository.findAll(
                PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"))
        );

        List<Merchant> items = page.getContent().stream()
                .map(MerchantPersistenceMapper::toDomain)
                .toList();

        return PagedResult.<Merchant>builder()
                .items(items)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
