package vn.com.routex.hub.management.service.domain.merchant.port;


import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.merchant.model.Merchant;

import java.util.Optional;

public interface MerchantRepositoryPort {

    Merchant save(Merchant merchant);

    Optional<Merchant> findById(String id);

    boolean existsByCode(String code);

    String generateMerchantCode();

    PagedResult<Merchant> fetch(int pageNumber, int pageSize);
}
