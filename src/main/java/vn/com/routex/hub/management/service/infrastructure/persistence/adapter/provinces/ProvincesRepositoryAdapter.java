package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.provinces;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.provinces.model.Province;
import vn.com.routex.hub.management.service.domain.provinces.port.ProvincesRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.provinces.repository.ProvincesEntityRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProvincesRepositoryAdapter implements ProvincesRepositoryPort {

    private final ProvincesEntityRepository provincesEntityRepository;
    private final ProvincesPersistenceMapper provincesPersistenceMapper;

    @Override
    public Optional<Province> findById(Integer id) {
        return provincesEntityRepository.findById(id).map(provincesPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByCode(String code) {
        return provincesEntityRepository.existsByCode(code);
    }

    @Override
    public boolean existsByName(String name) {
        return provincesEntityRepository.existsByName(name);
    }

    @Override
    public Province save(Province province) {
        return provincesPersistenceMapper.toDomain(
                provincesEntityRepository.save(provincesPersistenceMapper.toEntity(province))
        );
    }

    @Override
    public void deleteById(Integer id) {
        provincesEntityRepository.deleteById(id);
    }
}

