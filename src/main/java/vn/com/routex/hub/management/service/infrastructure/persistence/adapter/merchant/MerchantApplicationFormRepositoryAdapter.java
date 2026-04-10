package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.merchant;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.merchant.ApplicationFormStatus;
import vn.com.routex.hub.management.service.domain.merchant.model.MerchantApplicationForm;
import vn.com.routex.hub.management.service.domain.merchant.port.MerchantApplicationFormRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.merchant.entity.MerchantApplicationFormEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.merchant.repository.MerchantApplicationFormEntityRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MerchantApplicationFormRepositoryAdapter implements MerchantApplicationFormRepositoryPort {

    private final MerchantApplicationFormEntityRepository merchantApplicationFormEntityRepository;

    @Override
    public MerchantApplicationForm save(MerchantApplicationForm merchantApplicationForm) {
        MerchantApplicationFormEntity saved = merchantApplicationFormEntityRepository.save(
                MerchantApplicationFormPersistenceMapper.toEntity(merchantApplicationForm)
        );
        return MerchantApplicationFormPersistenceMapper.toDomain(saved);
    }

    @Override
    public boolean existsByFormCode(String formCode) {
        return merchantApplicationFormEntityRepository.existsByFormCode(formCode);
    }

    @Override
    public String generateFormCode() {
        return merchantApplicationFormEntityRepository.generateFormCode();
    }

    @Override
    public Optional<MerchantApplicationForm> findById(String id) {
        return merchantApplicationFormEntityRepository.findById(id)
                .map(MerchantApplicationFormPersistenceMapper::toDomain);
    }

    @Override
    public PagedResult<MerchantApplicationForm> fetch(int pageNumber, int pageSize) {
        Page<MerchantApplicationFormEntity> page = merchantApplicationFormEntityRepository.findAll(
                PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "submittedAt"))
        );
        return toPagedResult(page);
    }

    @Override
    public PagedResult<MerchantApplicationForm> fetchByStatus(ApplicationFormStatus status, int pageNumber, int pageSize) {
        Page<MerchantApplicationFormEntity> page = merchantApplicationFormEntityRepository.findByStatus(
                status,
                PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "submittedAt"))
        );
        return toPagedResult(page);
    }

    private PagedResult<MerchantApplicationForm> toPagedResult(Page<MerchantApplicationFormEntity> page) {
        List<MerchantApplicationForm> items = page.getContent().stream()
                .map(MerchantApplicationFormPersistenceMapper::toDomain)
                .toList();

        return PagedResult.<MerchantApplicationForm>builder()
                .items(items)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
