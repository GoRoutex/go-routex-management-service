package vn.com.routex.hub.management.service.domain.route.model;

import lombok.Builder;

@Builder
public record LocationCodePair(String originCode, String destinationCode) {
}
