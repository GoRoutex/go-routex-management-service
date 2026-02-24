package vn.com.routex.hub.user.service.domain.user;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.user.service.domain.auditing.AbstractAuditingEntity;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "SYS_USER")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User extends AbstractAuditingEntity {

    @Id
    private String id;
    private String username;
    private String fullName;
    private String pinCode;
    private String dob;
    private String phoneNumber;
    private String email;
    private UserStatus status;
    private String role;
    private String tenantId;
    private String language;
    private String timezone;
    private Integer failLoginCount;
    private OffsetDateTime lastLoginAt;
}
