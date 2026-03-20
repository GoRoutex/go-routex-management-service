package vn.com.routex.hub.management.service.domain.route.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationCodePair {
    private final String originCode;
    private final String destinationCode;
}
