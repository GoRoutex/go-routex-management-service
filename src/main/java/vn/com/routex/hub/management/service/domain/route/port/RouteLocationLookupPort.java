package vn.com.routex.hub.management.service.domain.route.port;

import vn.com.routex.hub.management.service.domain.route.model.LocationCodePair;

public interface RouteLocationLookupPort {
    LocationCodePair getCodes(String origin, String destination);
}
