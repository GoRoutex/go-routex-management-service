package vn.com.routex.hub.management.service.infrastructure.persistence.adapter.provinces;

import org.springframework.stereotype.Component;
import vn.com.routex.hub.management.service.domain.provinces.model.Province;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.provinces.entity.ProvincesEntity;

@Component
public class ProvincesPersistenceMapper {
    public Province toDomain(ProvincesEntity entity) {
        if (entity == null) return null;
        return Province.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .build();

    }

    public ProvincesEntity toEntity(Province province) {
        if (province == null) return null;
        ProvincesEntity entity = new ProvincesEntity();
        if (province.getId() != null) entity.setId(province.getId());
        entity.setName(province.getName());
        entity.setCode(province.getCode());
        return entity;

    }
}

