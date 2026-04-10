package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.provinces;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.provinces.model.Province;
import vn.com.routex.hub.management.service.domain.provinces.port.ProvincesRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.provinces.entity.MerchantProvinceEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.provinces.repository.MerchantProvinceEntityRepository;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.provinces.repository.ProvincesEntityRepository;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProvincesRepositoryAdapter implements ProvincesRepositoryPort {

    private final ProvincesEntityRepository provincesEntityRepository;
    private final MerchantProvinceEntityRepository merchantProvinceEntityRepository;
    private final ProvincesPersistenceMapper provincesPersistenceMapper;

    @Override
    public Optional<Province> findById(Integer id) {
        return provincesEntityRepository.findById(id).map(provincesPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Province> findByCode(String code) {
        return provincesEntityRepository.findByCode(code).map(provincesPersistenceMapper::toDomain);
    }

    @Override
    public boolean isAssigned(Integer provinceId, String merchantId) {
        return merchantProvinceEntityRepository.existsByMerchantIdAndProvinceId(merchantId, provinceId);
    }

    @Override
    public void assign(Integer provinceId, String merchantId) {
        if (!isAssigned(provinceId, merchantId)) {
            MerchantProvinceEntity mapping = MerchantProvinceEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .merchantId(merchantId)
                    .provinceId(provinceId)
                    .status("ACTIVE")
                    .build();
            merchantProvinceEntityRepository.save(mapping);
        }
    }

    @Override
    public void unassign(Integer provinceId, String merchantId) {
        merchantProvinceEntityRepository.findByMerchantIdAndProvinceId(merchantId, provinceId)
                .ifPresent(merchantProvinceEntityRepository::delete);
    }
}
