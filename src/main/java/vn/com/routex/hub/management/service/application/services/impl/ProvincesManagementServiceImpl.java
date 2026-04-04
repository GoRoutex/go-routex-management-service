package vn.com.routex.hub.management.service.application.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.hub.management.service.application.command.provinces.FetchProvincesQuery;
import vn.com.routex.hub.management.service.application.command.provinces.FetchProvincesResult;
import vn.com.routex.hub.management.service.application.command.provinces.CreateProvinceCommand;
import vn.com.routex.hub.management.service.application.command.provinces.CreateProvinceResult;
import vn.com.routex.hub.management.service.application.command.provinces.UpdateProvinceCommand;
import vn.com.routex.hub.management.service.application.command.provinces.UpdateProvinceResult;
import vn.com.routex.hub.management.service.application.command.provinces.DeleteProvinceCommand;
import vn.com.routex.hub.management.service.application.command.provinces.DeleteProvinceResult;
import vn.com.routex.hub.management.service.application.command.provinces.SearchProvincesQuery;
import vn.com.routex.hub.management.service.application.command.provinces.SearchProvincesResult;
import vn.com.routex.hub.management.service.application.services.ProvincesManagementService;
import vn.com.routex.hub.management.service.domain.common.PagedResult;
import vn.com.routex.hub.management.service.domain.provinces.port.ProvincesQueryPort;
import vn.com.routex.hub.management.service.domain.provinces.readmodel.ProvincesFetchView;
import vn.com.routex.hub.management.service.domain.provinces.model.Province;
import vn.com.routex.hub.management.service.domain.provinces.port.ProvincesRepositoryPort;
import vn.com.routex.hub.management.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.DateTimeUtils;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.ExceptionUtils;

import java.util.List;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_NUMBER;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_SIZE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_PROVINCE;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ErrorConstant.PROVINCE_NOT_FOUND;
import static vn.com.routex.hub.management.service.infrastructure.persistence.utils.ApiRequestUtils.parseIntOrDefault;

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

        PagedResult<ProvincesFetchView> page = provincesQueryPort.fetchRoutes(pageNumber - 1, pageSize);
        List<ProvincesFetchView> provinces = page.getItems();

        List<FetchProvincesResult.FetchProvinceResult> resultList = provinces.stream()
                .map(p -> {
                    return FetchProvincesResult.FetchProvinceResult
                            .builder()
                            .id(p.getId())
                            .name(p.getName())
                            .code(p.getCode())
                            .build();
                })
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
        String name = command.name() == null ? null : command.name().trim();
        String code = command.code() == null ? null : command.code().trim();

        if (name == null || name.isBlank() || code == null || code.isBlank()) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, "name and code are required"));
        }

        if (provincesRepositoryPort.existsByName(name) || provincesRepositoryPort.existsByCode(code)) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(DUPLICATE_PROVINCE, code)));
        }

        Province saved = provincesRepositoryPort.save(Province.builder()
                .name(name)
                .code(code)
                .build());

        return CreateProvinceResult.builder()
                .id(saved.getId())
                .name(saved.getName())
                .code(saved.getCode())
                .build();
    }

    @Override
    public UpdateProvinceResult updateProvince(UpdateProvinceCommand command) {
        Province existing = provincesRepositoryPort.findById(command.id())
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(PROVINCE_NOT_FOUND, command.id()))));

        String name = command.name() == null ? null : command.name().trim();
        String code = command.code() == null ? null : command.code().trim();

        if (name != null && !name.isBlank() && !name.equals(existing.getName()) && provincesRepositoryPort.existsByName(name)) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(DUPLICATE_PROVINCE, name)));
        }
        if (code != null && !code.isBlank() && !code.equals(existing.getCode()) && provincesRepositoryPort.existsByCode(code)) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(DUPLICATE_PROVINCE, code)));
        }

        Province saved = provincesRepositoryPort.save(existing.toBuilder()
                .name(name == null || name.isBlank() ? existing.getName() : name)
                .code(code == null || code.isBlank() ? existing.getCode() : code)
                .build());

        return UpdateProvinceResult.builder()
                .id(saved.getId())
                .name(saved.getName())
                .code(saved.getCode())
                .build();
    }

    @Override
    public DeleteProvinceResult deleteProvince(DeleteProvinceCommand command) {
        Province existing = provincesRepositoryPort.findById(command.id())
                .orElseThrow(() -> new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(PROVINCE_NOT_FOUND, command.id()))));
        provincesRepositoryPort.deleteById(existing.getId());
        return DeleteProvinceResult.builder()
                .id(existing.getId())
                .build();
    }
}
