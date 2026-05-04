package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.route.entity;


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
import vn.com.routex.hub.management.service.domain.auditing.AbstractAuditingEntity;
import vn.com.routex.hub.management.service.domain.route.RouteStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "ROUTE")
public class RouteEntity extends AbstractAuditingEntity {

    @Id
    private String id;

    @Column(name = "ORIGIN_CODE")
    private String originCode;

    @Column(name = "DESTINATION_CODE")
    private String destinationCode;

    @Column(name = "ORIGIN_NAME")
    private String originName;

    @Column(name = "DESTINATION_NAME")
    private String destinationName;

    @Column(name = "CREATOR")
    private String creator;

    @Column(name = "MERCHANT_ID")
    private String merchantId;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private RouteStatus status;
}
