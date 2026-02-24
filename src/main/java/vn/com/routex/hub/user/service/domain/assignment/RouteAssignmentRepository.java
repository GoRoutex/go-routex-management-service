package vn.com.routex.hub.user.service.domain.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteAssignmentRepository extends JpaRepository<RouteAssignment, String> {
}
