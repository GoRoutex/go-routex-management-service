package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.provinces.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.provinces.entity.ProvincesEntity;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.provinces.projection.ProvincesCodeProjection;

public interface ProvincesEntityRepository extends JpaRepository<ProvincesEntity, Integer> {

    @Query(value = """
            
            SELECT * FROM PROVINCES
                        WHERE (:keyword IS NULL OR :keyword = '' OR 
                                           LOWER(NAME) LIKE LOWER(CONCAT('%', :keyword, '%')) OR 
                                           LOWER(CODE) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """, nativeQuery = true)
    Page<ProvincesEntity> search(@Param("keyword") String kw, Pageable pageable);


    @Query(value = """
            SELECT  o.code AS originCode,
                    d.code AS destinationCode
            FROM PROVINCES o
            JOIN PROVINCES d
            ON d.name = :destination
            WHERE o.name = :origin
            """, nativeQuery = true)
    ProvincesCodeProjection selectProvincesCode(@Param("origin") String origin,
                                                @Param("destination") String destination);

}
