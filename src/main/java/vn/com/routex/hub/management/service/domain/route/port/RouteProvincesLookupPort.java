package vn.com.routex.hub.management.service.domain.route.port;

import vn.com.routex.hub.management.service.domain.route.model.ProvincesCodePair;

public interface RouteProvincesLookupPort {
    ProvincesCodePair getCodes(String origin, String destination);
}
