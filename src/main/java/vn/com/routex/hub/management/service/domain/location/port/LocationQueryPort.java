package vn.com.routex.hub.management.service.domain.location.port;

import vn.com.routex.hub.management.service.domain.location.readmodel.LocationSearchItem;

import java.util.List;

public interface LocationQueryPort {
    List<LocationSearchItem> search(String keyword, int page, int size);
}
