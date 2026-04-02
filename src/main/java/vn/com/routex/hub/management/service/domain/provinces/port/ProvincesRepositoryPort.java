package vn.com.routex.hub.management.service.domain.provinces.port;

import vn.com.routex.hub.management.service.domain.provinces.model.Province;

import java.util.Optional;

public interface ProvincesRepositoryPort {
    Optional<Province> findById(Integer id);

    boolean existsByCode(String code);

    boolean existsByName(String name);

    Province save(Province province);

    void deleteById(Integer id);
}

