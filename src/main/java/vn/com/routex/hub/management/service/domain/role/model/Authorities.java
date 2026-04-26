package vn.com.routex.hub.management.service.domain.role.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.management.service.domain.auditing.AbstractAuditingEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Authorities extends AbstractAuditingEntity {
    private int id;

    private String code;

    private String name;

    private String description;

    private Boolean enabled;

}
