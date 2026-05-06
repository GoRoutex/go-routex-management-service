package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.department.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.department.DepartmentStatus;
import vn.com.routex.hub.management.service.domain.department.DepartmentType;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "DEPARTMENT")
public class DepartmentEntity extends AbstractAuditingEntity {
    @Id
    private String id;

    @Column(name = "MERCHANT_ID")
    private String merchantId;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private DepartmentType type;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "CITY")
    private String city;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "LONGITUDE")
    private Double longitude;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private DepartmentStatus status;
}
