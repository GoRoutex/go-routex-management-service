package vn.com.routex.hub.management.service.domain.route.port;

import vn.com.routex.hub.management.service.domain.route.model.ProvincesInformationPair;

public interface RouteProvincesLookupPort {
    ProvincesInformationPair getCodes(String origin, String destination);
}
