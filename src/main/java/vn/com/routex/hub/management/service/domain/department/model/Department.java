package vn.com.routex.hub.management.service.domain.department.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.hub.management.service.domain.department.DepartmentStatus;
import vn.com.routex.hub.management.service.domain.department.DepartmentType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Department extends AbstractAuditingEntity {
    private String id;
    private String merchantId;
    private String code;
    private String name;
    private DepartmentType type;
    private String address;
    private String city;
    private Double latitude;
    private Double longitude;
    private DepartmentStatus status;
}
