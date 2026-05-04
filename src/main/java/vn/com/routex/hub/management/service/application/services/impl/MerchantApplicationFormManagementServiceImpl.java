package vn.com.routex.hub.management.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.management.service.application.command.merchant.FetchMerchantApplicationFormDetailQuery;
import vn.com.routex.hub.management.service.application.command.merchant.FetchMerchantApplicationFormDetailResult;
import vn.com.routex.hub.management.service.application.command.merchant.FetchPendingMerchantApplicationFormsQuery;
import vn.com.routex.hub.management.service.application.command.merchant.FetchPendingMerchantApplicationFormsResult;
import vn.com.routex.hub.management.service.application.services.MerchantApplicationFormManagementService;
import vn.com.routex.hub.management.service.domain.merchant.ApplicationFormStatus;
import vn.com.routex.hub.management.service.infrastructure.integration.common.support.InternalApiExecutor;
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.management.service.infrastructure.integration.merchantplatform.client.MerchantPlatformInternalClient;
import vn.com.routex.hub.management.service.infrastructure.integration.merchantplatform.model.MerchantPlatformInternalModels;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ExceptionUtils;

import java.util.List;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_NUMBER;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_SIZE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.MERCHANT_APPLICATION_FORM_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MerchantApplicationFormManagementServiceImpl implements MerchantApplicationFormManagementService {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 1;

    private final MerchantPlatformInternalClient merchantPlatformInternalClient;

    @Override
    public FetchPendingMerchantApplicationFormsResult fetchPendingApplicationForms(FetchPendingMerchantApplicationFormsQuery query) {
        int pageSize = ApiRequestUtils.parseIntOrDefault(query.pageSize(), DEFAULT_PAGE_SIZE, "pageSize",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());
        int pageNumber = ApiRequestUtils.parseIntOrDefault(query.pageNumber(), DEFAULT_PAGE_NUMBER, "pageNumber",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());
        ApplicationFormStatus status = parseStatus(query.status(), query);

        if (pageSize < 1 || pageSize > 100) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_SIZE));
        }
        if (pageNumber < 1) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_NUMBER));
        }

        MerchantPlatformInternalModels.MerchantApplicationFormPage page = InternalApiExecutor.execute(
                query.context(),
                () -> merchantPlatformInternalClient.fetchApplicationForms(status, pageNumber, pageSize)
        );

        List<FetchPendingMerchantApplicationFormsResult.PendingMerchantApplicationFormItemResult> items = page.getItems().stream()
                .map(form -> FetchPendingMerchantApplicationFormsResult.PendingMerchantApplicationFormItemResult.builder()
                        .id(form.getId())
                        .formCode(form.getFormCode())
                        .displayName(form.getDisplayName())
                        .legalName(form.getLegalName())
                        .taxCode(form.getTaxCode())
                        .businessLicense(form.getBusinessLicense())
                        .businessLicenseUrl(form.getBusinessLicenseUrl())
                        .country(form.getCountry())
                        .province(form.getProvince())
                        .address(form.getAddress())
                        .ward(form.getWard())
                        .postalCode(form.getPostalCode())
                        .logoUrl(form.getLogoUrl())
                        .description(form.getDescription())
                        .slug(form.getSlug())
                        .submittedBy(form.getSubmittedBy())
                        .submittedAt(form.getSubmittedAt())
                        .status(form.getStatus())
                        .contactName(form.getContactName())
                        .contactPhone(form.getContactPhone())
                        .contactEmail(form.getContactEmail())
                        .build())
                .toList();

        return FetchPendingMerchantApplicationFormsResult.builder()
                .items(items)
                .pageNumber(page.getPagination().getPageNumber())
                .pageSize(page.getPagination().getPageSize())
                .totalElements(page.getPagination().getTotalElements())
                .totalPages(page.getPagination().getTotalPages())
                .build();
    }

    private ApplicationFormStatus parseStatus(
            String rawStatus,
            FetchPendingMerchantApplicationFormsQuery query
    ) {
        if (rawStatus == null || rawStatus.isBlank()) {
            return null;
        }

        try {
            return ApplicationFormStatus.valueOf(rawStatus.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(
                    query.context().requestId(),
                    query.context().requestDateTime(),
                    query.context().channel(),
                    ExceptionUtils.buildResultResponse(
                            INVALID_INPUT_ERROR,
                            "status must be one of: SUBMITTED, APPROVED, REJECTED"
                    )
            );
        }
    }

    @Override
    public FetchMerchantApplicationFormDetailResult fetchApplicationFormDetail(FetchMerchantApplicationFormDetailQuery query) {
        MerchantPlatformInternalModels.MerchantApplicationFormData form = InternalApiExecutor.execute(
                query.context(),
                () -> merchantPlatformInternalClient.fetchApplicationFormDetail(query.applicationFormId())
        );

        return FetchMerchantApplicationFormDetailResult.builder()
                .id(form.getId())
                .formCode(form.getFormCode())
                .displayName(form.getDisplayName())
                .legalName(form.getLegalName())
                .taxCode(form.getTaxCode())
                .businessLicense(form.getBusinessLicense())
                .businessLicenseUrl(form.getBusinessLicenseUrl())
                .logoUrl(form.getLogoUrl())
                .address(FetchMerchantApplicationFormDetailResult.AddressResult.builder()
                        .country(form.getCountry())
                        .province(form.getProvince())
                        .address(form.getAddress())
                        .ward(form.getWard())
                        .postalCode(form.getPostalCode())
                        .build())
                .description(form.getDescription())
                .slug(form.getSlug())
                .approvedBy(form.getApprovedBy())
                .approvedAt(form.getApprovedAt())
                .rejectedBy(form.getRejectedBy())
                .rejectionReason(form.getRejectionReason())
                .status(form.getStatus())
                .submittedBy(form.getSubmittedBy())
                .submittedAt(form.getSubmittedAt())
                .contact(FetchMerchantApplicationFormDetailResult.ContactResult.builder()
                        .contactEmail(form.getContactEmail())
                        .contactName(form.getContactName())
                        .contactPhone(form.getContactPhone())
                        .build())
                .bankInfo(FetchMerchantApplicationFormDetailResult.BankInfoResult.builder()
                        .bankAccountName(form.getBankAccountName())
                        .bankAccountNumber(form.getBankAccountNumber())
                        .bankBranch(form.getBankBranch())
                        .bankName(form.getBankName())
                        .build())
                .ownerInfo(FetchMerchantApplicationFormDetailResult.OwnerInfoResult.builder()
                        .ownerEmail(form.getOwnerEmail())
                        .ownerFullName(form.getOwnerFullName())
                        .ownerName(form.getOwnerName())
                        .ownerPhone(form.getOwnerPhone())
                        .build())
                .build();
    }

}
