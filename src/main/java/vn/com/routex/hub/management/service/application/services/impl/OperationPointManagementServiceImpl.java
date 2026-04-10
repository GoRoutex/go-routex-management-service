package vn.com.routex.hub.management.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.management.service.application.command.operationpoint.CreateOperationPointCommand;
import vn.com.routex.hub.management.service.application.command.operationpoint.CreateOperationPointResult;
import vn.com.routex.hub.management.service.application.command.operationpoint.DeleteOperationPointCommand;
import vn.com.routex.hub.management.service.application.command.operationpoint.DeleteOperationPointResult;
import vn.com.routex.hub.management.service.application.command.operationpoint.FetchOperationPointQuery;
import vn.com.routex.hub.management.service.application.command.operationpoint.FetchOperationPointResult;
import vn.com.routex.hub.management.service.application.command.operationpoint.UpdateOperationPointCommand;
import vn.com.routex.hub.management.service.application.command.operationpoint.UpdateOperationPointResult;
import vn.com.routex.hub.management.service.application.services.OperationPointManagementService;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.operationpoint.OperationPointStatus;
import vn.com.routex.hub.management.service.domain.operationpoint.model.OperationPoint;
import vn.com.routex.hub.management.service.domain.operationpoint.port.OperationPointRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.DateTimeUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ExceptionUtils;

import java.util.List;
import java.util.UUID;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_OPERATION_POINT_MESSAGE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_NUMBER;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_SIZE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.OPERATION_POINT_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class OperationPointManagementServiceImpl implements OperationPointManagementService {

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());
    private final OperationPointRepositoryPort operationPointRepositoryPort;

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 1;

    @Override
    public CreateOperationPointResult createOperationPoint(CreateOperationPointCommand command) {
        boolean existPoint = operationPointRepositoryPort.existsByCode(command.code(), command.merchantId());
        if(existPoint) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, DUPLICATE_OPERATION_POINT_MESSAGE));
        }
        OperationPoint operationPoint = OperationPoint.builder()
                .id(UUID.randomUUID().toString())
                .merchantId(command.merchantId())
                .code(command.code())
                .name(command.name())
                .type(command.type())
                .address(command.address())
                .city(command.city())
                .latitude(command.latitude())
                .longitude(command.longitude())
                .status(command.status())
                .build();

        operationPointRepositoryPort.save(operationPoint);

        return CreateOperationPointResult.builder()
                .id(operationPoint.getId())
                .code(operationPoint.getCode())
                .name(operationPoint.getName())
                .type(operationPoint.getType())
                .address(operationPoint.getAddress())
                .city(operationPoint.getCity())
                .latitude(operationPoint.getLatitude())
                .longitude(operationPoint.getLongitude())
                .status(operationPoint.getStatus())
                .build();
    }

    @Override
    public UpdateOperationPointResult updateOperationPoint(UpdateOperationPointCommand command) {
        OperationPoint existing = operationPointRepositoryPort.findById(command.id(), command.merchantId())
                .orElseThrow(() -> new BusinessException(
                        command.context().requestId(),
                        command.context().requestDateTime(),
                        command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(OPERATION_POINT_NOT_FOUND, command.id()))
                ));

        if (command.code() != null && !command.code().isBlank() && !command.code().equals(existing.getCode())) {
            if (operationPointRepositoryPort.existsByCode(command.code(), command.merchantId())) {
                throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, DUPLICATE_OPERATION_POINT_MESSAGE));
            }
        }

        OperationPoint updated = OperationPoint.builder()
                .id(existing.getId())
                .merchantId(existing.getMerchantId())
                .code(firstNonBlank(command.code(), existing.getCode()))
                .name(firstNonBlank(command.name(), existing.getName()))
                .type(command.type() == null ? existing.getType() : command.type())
                .address(firstNonBlank(command.address(), existing.getAddress()))
                .city(firstNonBlank(command.city(), existing.getCity()))
                .latitude(command.latitude() == null ? existing.getLatitude() : command.latitude())
                .longitude(command.longitude() == null ? existing.getLongitude() : command.longitude())
                .status(command.status() == null ? existing.getStatus() : command.status())
                .createdAt(existing.getCreatedAt())
                .createdBy(existing.getCreatedBy())
                .updatedAt(existing.getUpdatedAt())
                .updatedBy(existing.getUpdatedBy())
                .build();

        operationPointRepositoryPort.save(updated);

        return UpdateOperationPointResult.builder()
                .id(updated.getId())
                .code(updated.getCode())
                .name(updated.getName())
                .type(updated.getType())
                .address(updated.getAddress())
                .city(updated.getCity())
                .latitude(updated.getLatitude())
                .longitude(updated.getLongitude())
                .status(updated.getStatus())
                .build();
    }

    @Override
    public DeleteOperationPointResult deleteOperationPoint(DeleteOperationPointCommand command) {
        OperationPoint existing = operationPointRepositoryPort.findById(command.id(), command.merchantId())
                .orElseThrow(() -> new BusinessException(
                        command.context().requestId(),
                        command.context().requestDateTime(),
                        command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(OPERATION_POINT_NOT_FOUND, command.id()))
                ));

        OperationPoint closed = OperationPoint.builder()
                .id(existing.getId())
                .merchantId(existing.getMerchantId())
                .code(existing.getCode())
                .name(existing.getName())
                .type(existing.getType())
                .address(existing.getAddress())
                .city(existing.getCity())
                .latitude(existing.getLatitude())
                .longitude(existing.getLongitude())
                .status(OperationPointStatus.CLOSED)
                .createdAt(existing.getCreatedAt())
                .createdBy(existing.getCreatedBy())
                .updatedAt(existing.getUpdatedAt())
                .updatedBy(existing.getUpdatedBy())
                .build();

        operationPointRepositoryPort.save(closed);

        return DeleteOperationPointResult.builder()
                .id(closed.getId())
                .code(closed.getCode())
                .status(closed.getStatus())
                .build();
    }

    @Override
    public FetchOperationPointResult fetchOperationPoint(FetchOperationPointQuery query) {
        int pageSize = parseIntOrDefault(query.pageSize(), DEFAULT_PAGE_SIZE, "pageSize",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());
        int pageNumber = parseIntOrDefault(query.pageNumber(), DEFAULT_PAGE_NUMBER, "pageNumber",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());

        if (pageSize < 1 || pageSize > 100) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_SIZE));
        }
        if (pageNumber < 1) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_NUMBER));
        }

        PagedResult<OperationPoint> page = operationPointRepositoryPort.fetch(query.merchantId(), pageNumber - 1, pageSize);
        List<FetchOperationPointResult.FetchOperationPointItemResult> items = page.getItems().stream()
                .map(p -> FetchOperationPointResult.FetchOperationPointItemResult.builder()
                        .id(p.getId())
                        .code(p.getCode())
                        .name(p.getName())
                        .type(p.getType())
                        .address(p.getAddress())
                        .city(p.getCity())
                        .latitude(p.getLatitude())
                        .longitude(p.getLongitude())
                        .status(p.getStatus())
                        .build())
                .toList();

        return FetchOperationPointResult.builder()
                .items(items)
                .pageNumber(page.getPageNumber() + 1)
                .pageSize(page.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    private static int parseIntOrDefault(
            String v,
            int defaultValue,
            String field,
            String requestId,
            String requestDateTime,
            String channel
    ) {
        if (v == null || v.isBlank()) return defaultValue;
        return DateTimeUtils.parseIntOrThrow(v, field, requestId, requestDateTime, channel);
    }

    private static String firstNonBlank(String value, String fallback) {
        return (value == null || value.isBlank()) ? fallback : value;
    }
}
