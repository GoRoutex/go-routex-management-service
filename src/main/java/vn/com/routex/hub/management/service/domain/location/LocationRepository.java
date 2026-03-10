package vn.com.routex.hub.management.service.domain.location;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.routex.hub.management.service.interfaces.models.location.LocationCodeProjection;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    @Query(value = """
            
            SELECT * FROM LOCATION
                        WHERE (:keyword IS NULL OR :keyword = '' OR 
                                           LOWER(NAME) LIKE LOWER(CONCAT('%', :keyword, '%')) OR 
                                           LOWER(CODE) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """, nativeQuery = true)
    Page<Location> search(@Param("keyword") String kw, Pageable pageable);


    @Query(value = """
            SELECT  o.code AS originCode,
                    d.code AS destinationCode
            FROM LOCATION o
            JOIN LOCATION d
            ON d.name = :destination
            WHERE o.name = :origin
            """, nativeQuery = true)
    LocationCodeProjection selectLocationCode(@Param("origin") String origin,
                                              @Param("destination") String destination);

}
