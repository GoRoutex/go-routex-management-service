package vn.com.routex.hub.management.service.domain.provinces.port;

import vn.com.routex.hub.management.service.domain.provinces.readmodel.ProvincesSearchItem;

import java.util.List;

public interface ProvincesQueryPort {
    List<ProvincesSearchItem> search(String keyword, int page, int size);
}
