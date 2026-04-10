package vn.com.routex.hub.management.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.management.service.application.command.merchant.FetchMerchantsQuery;
import vn.com.routex.hub.management.service.application.command.merchant.FetchMerchantsResult;
import vn.com.routex.hub.management.service.application.command.merchant.UpdateMerchantCommand;
import vn.com.routex.hub.management.service.application.command.merchant.UpdateMerchantResult;
import vn.com.routex.hub.management.service.application.services.MerchantManagementService;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.merchant.ApplicationFormStatus;
import vn.com.routex.hub.management.service.domain.merchant.model.Merchant;
import vn.com.routex.hub.management.service.domain.merchant.port.MerchantRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.management.service.infrastructure.persistence.jpa.merchant.repository.MerchantApplicationFormEntityRepository;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ExceptionUtils;

import java.math.BigDecimal;
import java.util.List;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_COMMISSION_RATE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_NUMBER;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_SIZE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.MERCHANT_NOT_FOUND_BY_ID;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MerchantManagementServiceImpl implements MerchantManagementService {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 1;

    private final MerchantRepositoryPort merchantRepositoryPort;
    private final MerchantApplicationFormEntityRepository merchantApplicationFormEntityRepository;

    @Override
    public FetchMerchantsResult fetchMerchants(FetchMerchantsQuery query) {
        int pageSize = ApiRequestUtils.parseIntOrDefault(query.pageSize(), DEFAULT_PAGE_SIZE, "pageSize",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());
        int pageNumber = ApiRequestUtils.parseIntOrDefault(query.pageNumber(), DEFAULT_PAGE_NUMBER, "pageNumber",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());

        if (pageSize < 1 || pageSize > 100) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_SIZE));
        }
        if (pageNumber < 1) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_NUMBER));
        }

        PagedResult<Merchant> page = merchantRepositoryPort.fetch(pageNumber - 1, pageSize);
        List<FetchMerchantsResult.FetchMerchantItemResult> items = page.getItems().stream()
                .map(merchant -> FetchMerchantsResult.FetchMerchantItemResult.builder()
                        .id(merchant.getId())
                        .code(merchant.getCode())
                        .name(merchant.getName())
                        .taxCode(merchant.getTaxCode())
                        .phone(merchant.getPhone())
                        .email(merchant.getEmail())
                        .address(merchant.getAddress())
                        .representativeName(merchant.getRepresentativeName())
                        .commissionRate(merchant.getCommissionRate())
                        .status(merchant.getStatus())
                        .build())
                .toList();

        return FetchMerchantsResult.builder()
                .items(items)
                .totalPartners(page.getTotalElements())
                .totalRevenueShare(BigDecimal.ZERO)
                .avgRating(BigDecimal.ZERO)
                .numberOfPendingApps(merchantApplicationFormEntityRepository.countByStatus(ApplicationFormStatus.SUBMITTED))
                .pageNumber(page.getPageNumber() + 1)
                .pageSize(page.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public UpdateMerchantResult updateMerchant(UpdateMerchantCommand command) {
        Merchant existing = merchantRepositoryPort.findById(command.merchantId())
                .orElseThrow(() -> new BusinessException(
                        command.context().requestId(),
                        command.context().requestDateTime(),
                        command.context().channel(),
                        ExceptionUtils.buildResultResponse(
                                RECORD_NOT_FOUND,
                                String.format(MERCHANT_NOT_FOUND_BY_ID, command.merchantId())
                        )
                ));

        validateCommissionRate(command.commissionRate(), command);

        Merchant updated = existing.toBuilder()
                .name(ApiRequestUtils.firstNonBlank(command.name(), existing.getName()))
                .taxCode(ApiRequestUtils.firstNonBlank(command.taxCode(), existing.getTaxCode()))
                .phone(ApiRequestUtils.firstNonBlank(command.phone(), existing.getPhone()))
                .email(ApiRequestUtils.firstNonBlank(command.email(), existing.getEmail()))
                .address(ApiRequestUtils.firstNonBlank(command.address(), existing.getAddress()))
                .representativeName(ApiRequestUtils.firstNonBlank(command.representativeName(), existing.getRepresentativeName()))
                .commissionRate(command.commissionRate() == null ? existing.getCommissionRate() : command.commissionRate())
                .status(command.status() == null ? existing.getStatus() : command.status())
                .updatedBy(ApiRequestUtils.firstNonBlank(command.updatedBy(), existing.getUpdatedBy()))
                .build();

        Merchant saved = merchantRepositoryPort.save(updated);

        return UpdateMerchantResult.builder()
                .id(saved.getId())
                .code(saved.getCode())
                .name(saved.getName())
                .taxCode(saved.getTaxCode())
                .phone(saved.getPhone())
                .email(saved.getEmail())
                .address(saved.getAddress())
                .representativeName(saved.getRepresentativeName())
                .commissionRate(saved.getCommissionRate())
                .status(saved.getStatus())
                .build();
    }

    private void validateCommissionRate(BigDecimal commissionRate, UpdateMerchantCommand command) {
        if (commissionRate == null) {
            return;
        }

        if (commissionRate.compareTo(BigDecimal.ZERO) < 0 || commissionRate.compareTo(new BigDecimal("100")) > 0) {
            throw new BusinessException(
                    command.context().requestId(),
                    command.context().requestDateTime(),
                    command.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_COMMISSION_RATE)
            );
        }
    }
}
