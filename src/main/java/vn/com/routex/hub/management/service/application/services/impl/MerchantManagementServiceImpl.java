package vn.com.routex.hub.management.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.management.service.application.command.merchant.FetchMerchantDetailQuery;
import vn.com.routex.hub.management.service.application.command.merchant.FetchMerchantDetailResult;
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
    public FetchMerchantDetailResult fetchMerchantDetail(FetchMerchantDetailQuery query) {
        Merchant merchant = merchantRepositoryPort.findById(query.merchantId())
                .orElseThrow(() -> new BusinessException(
                        query.context().requestId(),
                        query.context().requestDateTime(),
                        query.context().channel(),
                        ExceptionUtils.buildResultResponse(
                                RECORD_NOT_FOUND,
                                String.format(MERCHANT_NOT_FOUND_BY_ID, query.merchantId())
                        )
                ));

        return FetchMerchantDetailResult.builder()
                .id(merchant.getId())
                .code(merchant.getCode())
                .slug(merchant.getSlug())
                .displayName(merchant.getDisplayName())
                .legalName(merchant.getLegalName())
                .taxCode(merchant.getTaxCode())
                .businessLicenseNumber(merchant.getBusinessLicenseNumber())
                .businessLicenseUrl(merchant.getBusinessLicenseUrl())
                .phone(merchant.getPhone())
                .email(merchant.getEmail())
                .logoUrl(merchant.getLogoUrl())
                .description(merchant.getDescription())
                .address(merchant.getAddress())
                .ward(merchant.getWard())
                .province(merchant.getProvince())
                .country(merchant.getCountry())
                .postalCode(merchant.getPostalCode())
                .representativeName(merchant.getRepresentativeName())
                .contactName(merchant.getContactName())
                .contactPhone(merchant.getContactPhone())
                .contactEmail(merchant.getContactEmail())
                .ownerFullName(merchant.getOwnerFullName())
                .ownerPhone(merchant.getOwnerPhone())
                .ownerEmail(merchant.getOwnerEmail())
                .bankAccountName(merchant.getBankAccountName())
                .bankAccountNumber(merchant.getBankAccountNumber())
                .bankName(merchant.getBankName())
                .bankBranch(merchant.getBankBranch())
                .commissionRate(merchant.getCommissionRate())
                .status(merchant.getStatus())
                .approvedAt(merchant.getApprovedAt())
                .approvedBy(merchant.getApprovedBy())
                .build();
    }

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
                        .slug(merchant.getSlug())
                        .displayName(merchant.getDisplayName())
                        .legalName(merchant.getLegalName())
                        .taxCode(merchant.getTaxCode())
                        .businessLicenseNumber(merchant.getBusinessLicenseNumber())
                        .businessLicenseUrl(merchant.getBusinessLicenseUrl())
                        .phone(merchant.getPhone())
                        .email(merchant.getEmail())
                        .logoUrl(merchant.getLogoUrl())
                        .description(merchant.getDescription())
                        .address(merchant.getAddress())
                        .ward(merchant.getWard())
                        .province(merchant.getProvince())
                        .country(merchant.getCountry())
                        .postalCode(merchant.getPostalCode())
                        .representativeName(merchant.getRepresentativeName())
                        .contactName(merchant.getContactName())
                        .contactPhone(merchant.getContactPhone())
                        .contactEmail(merchant.getContactEmail())
                        .ownerFullName(merchant.getOwnerFullName())
                        .ownerPhone(merchant.getOwnerPhone())
                        .ownerEmail(merchant.getOwnerEmail())
                        .bankAccountName(merchant.getBankAccountName())
                        .bankAccountNumber(merchant.getBankAccountNumber())
                        .bankName(merchant.getBankName())
                        .bankBranch(merchant.getBankBranch())
                        .commissionRate(merchant.getCommissionRate())
                        .status(merchant.getStatus())
                        .approvedAt(merchant.getApprovedAt())
                        .approvedBy(merchant.getApprovedBy())
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
                .code(ApiRequestUtils.firstNonBlank(command.code(), existing.getCode()))
                .slug(ApiRequestUtils.firstNonBlank(command.slug(), existing.getSlug()))

                .displayName(ApiRequestUtils.firstNonBlank(command.displayName(), existing.getDisplayName()))
                .legalName(ApiRequestUtils.firstNonBlank(command.legalName(), existing.getLegalName()))

                .taxCode(ApiRequestUtils.firstNonBlank(command.taxCode(), existing.getTaxCode()))
                .businessLicenseNumber(ApiRequestUtils.firstNonBlank(
                        command.businessLicenseNumber(), existing.getBusinessLicenseNumber()))
                .businessLicenseUrl(ApiRequestUtils.firstNonBlank(
                        command.businessLicenseUrl(), existing.getBusinessLicenseUrl()))

                .phone(ApiRequestUtils.firstNonBlank(command.phone(), existing.getPhone()))
                .email(ApiRequestUtils.firstNonBlank(command.email(), existing.getEmail()))
                .logoUrl(ApiRequestUtils.firstNonBlank(command.logoUrl(), existing.getLogoUrl()))
                .description(ApiRequestUtils.firstNonBlank(command.description(), existing.getDescription()))

                .address(ApiRequestUtils.firstNonBlank(command.address(), existing.getAddress()))
                .ward(ApiRequestUtils.firstNonBlank(command.ward(), existing.getWard()))
                .province(ApiRequestUtils.firstNonBlank(command.province(), existing.getProvince()))
                .country(ApiRequestUtils.firstNonBlank(command.country(), existing.getCountry()))
                .postalCode(ApiRequestUtils.firstNonBlank(command.postalCode(), existing.getPostalCode()))

                .representativeName(ApiRequestUtils.firstNonBlank(
                        command.representativeName(), existing.getRepresentativeName()))

                .contactName(ApiRequestUtils.firstNonBlank(command.contactName(), existing.getContactName()))
                .contactPhone(ApiRequestUtils.firstNonBlank(command.contactPhone(), existing.getContactPhone()))
                .contactEmail(ApiRequestUtils.firstNonBlank(command.contactEmail(), existing.getContactEmail()))

                .ownerFullName(ApiRequestUtils.firstNonBlank(command.ownerFullName(), existing.getOwnerFullName()))
                .ownerPhone(ApiRequestUtils.firstNonBlank(command.ownerPhone(), existing.getOwnerPhone()))
                .ownerEmail(ApiRequestUtils.firstNonBlank(command.ownerEmail(), existing.getOwnerEmail()))

                .bankAccountName(ApiRequestUtils.firstNonBlank(
                        command.bankAccountName(), existing.getBankAccountName()))
                .bankAccountNumber(ApiRequestUtils.firstNonBlank(
                        command.bankAccountNumber(), existing.getBankAccountNumber()))
                .bankName(ApiRequestUtils.firstNonBlank(command.bankName(), existing.getBankName()))
                .bankBranch(ApiRequestUtils.firstNonBlank(command.bankBranch(), existing.getBankBranch()))

                .commissionRate(command.commissionRate() == null
                        ? existing.getCommissionRate()
                        : command.commissionRate())

                .status(command.status() == null
                        ? existing.getStatus()
                        : command.status())

                .approvedAt(command.approvedAt() == null
                        ? existing.getApprovedAt()
                        : command.approvedAt())

                .approvedBy(ApiRequestUtils.firstNonBlank(command.approvedBy(), existing.getApprovedBy()))
                .updatedBy(ApiRequestUtils.firstNonBlank(command.updatedBy(), existing.getUpdatedBy()))
                .build();

        Merchant saved = merchantRepositoryPort.save(updated);

        return UpdateMerchantResult.builder()
                .id(saved.getId())
                .code(saved.getCode())
                .slug(saved.getSlug())
                .displayName(saved.getDisplayName())
                .legalName(saved.getLegalName())
                .taxCode(saved.getTaxCode())
                .businessLicenseNumber(saved.getBusinessLicenseNumber())
                .businessLicenseUrl(saved.getBusinessLicenseUrl())
                .phone(saved.getPhone())
                .email(saved.getEmail())
                .logoUrl(saved.getLogoUrl())
                .description(saved.getDescription())
                .address(saved.getAddress())
                .ward(saved.getWard())
                .province(saved.getProvince())
                .country(saved.getCountry())
                .postalCode(saved.getPostalCode())
                .representativeName(saved.getRepresentativeName())
                .contactName(saved.getContactName())
                .contactPhone(saved.getContactPhone())
                .contactEmail(saved.getContactEmail())
                .ownerFullName(saved.getOwnerFullName())
                .ownerPhone(saved.getOwnerPhone())
                .ownerEmail(saved.getOwnerEmail())
                .bankAccountName(saved.getBankAccountName())
                .bankAccountNumber(saved.getBankAccountNumber())
                .bankName(saved.getBankName())
                .bankBranch(saved.getBankBranch())
                .commissionRate(saved.getCommissionRate())
                .status(saved.getStatus())
                .updatedBy(command.updatedBy())
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
