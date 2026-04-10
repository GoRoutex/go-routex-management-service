package vn.com.routex.hub.management.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.management.service.application.command.merchant.FetchMerchantApplicationFormDetailQuery;
import vn.com.routex.hub.management.service.application.command.merchant.FetchMerchantApplicationFormDetailResult;
import vn.com.routex.hub.management.service.application.command.merchant.FetchPendingMerchantApplicationFormsQuery;
import vn.com.routex.hub.management.service.application.command.merchant.FetchPendingMerchantApplicationFormsResult;
import vn.com.routex.hub.management.service.application.services.MerchantApplicationFormManagementService;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.merchant.ApplicationFormStatus;
import vn.com.routex.hub.management.service.domain.merchant.model.MerchantApplicationForm;
import vn.com.routex.hub.management.service.domain.merchant.port.MerchantApplicationFormRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
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

    private final MerchantApplicationFormRepositoryPort merchantApplicationFormRepositoryPort;

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

        PagedResult<MerchantApplicationForm> page = status == null
                ? merchantApplicationFormRepositoryPort.fetch(pageNumber - 1, pageSize)
                : merchantApplicationFormRepositoryPort.fetchByStatus(status, pageNumber - 1, pageSize);

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
                        .district(form.getDistrict())
                        .city(form.getCity())
                        .postalCode(form.getPostalCode())
                        .description(form.getDescription())
                        .slug(form.getSlug())
                        .merchantName(form.getMerchantName())
                        .submittedBy(form.getSubmittedBy())
                        .submittedAt(form.getSubmittedAt())
                        .status(form.getStatus())
                        .contactName(form.getContact() == null ? null : form.getContact().getContactName())
                        .contactPhone(form.getContact() == null ? null : form.getContact().getContactPhone())
                        .contactEmail(form.getContact() == null ? null : form.getContact().getContactEmail())
                        .build())
                .toList();

        return FetchPendingMerchantApplicationFormsResult.builder()
                .items(items)
                .pageNumber(page.getPageNumber() + 1)
                .pageSize(page.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
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
        MerchantApplicationForm form = merchantApplicationFormRepositoryPort.findById(query.applicationFormId())
                .orElseThrow(() -> new BusinessException(
                        query.context().requestId(),
                        query.context().requestDateTime(),
                        query.context().channel(),
                        ExceptionUtils.buildResultResponse(
                                RECORD_NOT_FOUND,
                                String.format(MERCHANT_APPLICATION_FORM_NOT_FOUND, query.applicationFormId())
                        )
                ));

        return FetchMerchantApplicationFormDetailResult.builder()
                .id(form.getId())
                .formCode(form.getFormCode())
                .displayName(form.getDisplayName())
                .legalName(form.getLegalName())
                .taxCode(form.getTaxCode())
                .businessLicense(form.getBusinessLicense())
                .businessLicenseUrl(form.getBusinessLicenseUrl())
                .country(form.getCountry())
                .province(form.getProvince())
                .district(form.getDistrict())
                .city(form.getCity())
                .postalCode(form.getPostalCode())
                .description(form.getDescription())
                .slug(form.getSlug())
                .merchantId(form.getMerchantId())
                .merchantName(form.getMerchantName())
                .approvedBy(form.getApprovedBy())
                .approvedAt(form.getApprovedAt())
                .rejectedBy(form.getRejectedBy())
                .rejectionReason(form.getRejectionReason())
                .status(form.getStatus())
                .submittedBy(form.getSubmittedBy())
                .submittedAt(form.getSubmittedAt())
                .contact(FetchMerchantApplicationFormDetailResult.ContactResult.builder()
                        .contactEmail(form.getContact() == null ? null : form.getContact().getContactEmail())
                        .contactName(form.getContact() == null ? null : form.getContact().getContactName())
                        .contactPhone(form.getContact() == null ? null : form.getContact().getContactPhone())
                        .build())
                .bankInfo(FetchMerchantApplicationFormDetailResult.BankInfoResult.builder()
                        .bankAccountName(form.getBankInfo() == null ? null : form.getBankInfo().getBankAccountName())
                        .bankAccountNumber(form.getBankInfo() == null ? null : form.getBankInfo().getBankAccountNumber())
                        .bankBranch(form.getBankInfo() == null ? null : form.getBankInfo().getBankBranch())
                        .bankName(form.getBankInfo() == null ? null : form.getBankInfo().getBankName())
                        .build())
                .ownerInfo(FetchMerchantApplicationFormDetailResult.OwnerInfoResult.builder()
                        .ownerEmail(form.getOwnerInfo() == null ? null : form.getOwnerInfo().getOwnerEmail())
                        .ownerFullName(form.getOwnerInfo() == null ? null : form.getOwnerInfo().getOwnerFullName())
                        .ownerName(form.getOwnerInfo() == null ? null : form.getOwnerInfo().getOwnerName())
                        .ownerPhone(form.getOwnerInfo() == null ? null : form.getOwnerInfo().getOwnerPhone())
                        .build())
                .build();
    }
}
