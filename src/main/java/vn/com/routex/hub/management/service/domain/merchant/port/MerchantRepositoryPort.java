package vn.com.routex.hub.management.service.domain.merchant.port;


import vn.com.routex.hub.management.service.domain.merchant.model.Merchant;

public interface MerchantRepositoryPort {

    Merchant save(Merchant merchant);

    boolean existsByCode(String code);

    String generateMerchantCode();
}
