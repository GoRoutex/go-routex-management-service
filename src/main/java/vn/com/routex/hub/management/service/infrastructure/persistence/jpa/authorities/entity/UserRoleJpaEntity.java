package vn.com.routex.hub.management.service.infrastructure.persistence.jpa.authorities.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "USER_ROLES")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleJpaEntity {

    @EmbeddedId
    private UserRoleJpaId id;

    private OffsetDateTime assignedAt;

}
