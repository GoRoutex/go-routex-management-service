package vn.com.routex.hub.management.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.management.service.application.command.provinces.CreateProvinceCommand;
import vn.com.routex.hub.management.service.application.command.provinces.CreateProvinceResult;
import vn.com.routex.hub.management.service.application.command.provinces.DeleteProvinceCommand;
import vn.com.routex.hub.management.service.application.command.provinces.DeleteProvinceResult;
import vn.com.routex.hub.management.service.application.command.provinces.FetchProvincesQuery;
import vn.com.routex.hub.management.service.application.command.provinces.FetchProvincesResult;
import vn.com.routex.hub.management.service.application.command.provinces.SearchProvincesQuery;
import vn.com.routex.hub.management.service.application.command.provinces.SearchProvincesResult;
import vn.com.routex.hub.management.service.application.command.provinces.UpdateProvinceCommand;
import vn.com.routex.hub.management.service.application.command.provinces.UpdateProvinceResult;
import vn.com.routex.hub.management.service.application.services.ProvincesManagementService;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.provinces.model.Province;
import vn.com.routex.hub.management.service.domain.provinces.port.ProvincesQueryPort;
import vn.com.routex.hub.management.service.domain.provinces.port.ProvincesRepositoryPort;
import vn.com.routex.hub.management.service.domain.provinces.readmodel.ProvincesFetchView;
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ExceptionUtils;

import java.util.List;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_NUMBER;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_SIZE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.PROVINCE_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProvincesManagementServiceImpl implements ProvincesManagementService {

    private final ProvincesQueryPort provincesQueryPort;
    private final ProvincesRepositoryPort provincesRepositoryPort;

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 1;

    @Override
    public SearchProvincesResult searchProvinces(SearchProvincesQuery query) {
        List<SearchProvincesResult.SearchProvincesItemResult> items = provincesQueryPort.search(
                        query.merchantId(),
                        query.keyword(),
                        query.page(),
                        query.size()
                ).stream()
                .map(item -> SearchProvincesResult.SearchProvincesItemResult.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .code(item.getCode())
                        .build())
                .toList();
        return SearchProvincesResult.builder()
                .data(items)
                .build();
    }

    @Override
    public FetchProvincesResult fetchProvinces(FetchProvincesQuery query) {
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

        PagedResult<ProvincesFetchView> page = provincesQueryPort.fetchRoutes(query.context().merchantId(), pageNumber - 1, pageSize);
        List<ProvincesFetchView> provinces = page.getItems();

        List<FetchProvincesResult.FetchProvinceResult> resultList = provinces.stream()
                .map(p -> FetchProvincesResult.FetchProvinceResult.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .code(p.getCode())
                        .build())
                .toList();

        return FetchProvincesResult.builder()
                            .items(resultList)
                            .pageNumber(page.getPageNumber() + 1)
                            .pageSize(page.getPageSize())
                            .totalElements(page.getTotalElements())
                            .totalPages(page.getTotalPages())
                            .build();
    }

    @Override
    public CreateProvinceResult createProvince(CreateProvinceCommand command) {
        String code = command.code() == null ? null : command.code().trim();

        if (code == null || code.isBlank()) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, "province code is required to assign"));
        }

        Province master = provincesRepositoryPort.findByCode(code)
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(PROVINCE_NOT_FOUND, code))));

        provincesRepositoryPort.assign(master.getId(), command.context().merchantId());

        return CreateProvinceResult.builder()
                .id(master.getId())
                .name(master.getName())
                .code(master.getCode())
                .build();
    }

    @Override
    public UpdateProvinceResult updateProvince(UpdateProvinceCommand command) {
        // For a Many-to-Many mapping, "Update" doesn't usually make sense unless we update mapping status.
        // We'll treat it as potentially re-assigning or just verifying if assigned.
        // Actually, we'll just throw not supported for merchants for now, or just return existing.
        Integer provinceId = Integer.parseInt(command.id());
        Province master = provincesRepositoryPort.findById(provinceId)
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(PROVINCE_NOT_FOUND, command.id()))));

        if (!provincesRepositoryPort.isAssigned(provinceId, command.context().merchantId())) {
             throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, "Province not assigned to this merchant"));
        }

        return UpdateProvinceResult.builder()
                .id(master.getId())
                .name(master.getName())
                .code(master.getCode())
                .build();
    }

    @Override
    public DeleteProvinceResult deleteProvince(DeleteProvinceCommand command) {
        provincesRepositoryPort.unassign(command.id(), command.context().merchantId());
        return DeleteProvinceResult.builder()
                .id(command.id())
                .build();
    }
}
