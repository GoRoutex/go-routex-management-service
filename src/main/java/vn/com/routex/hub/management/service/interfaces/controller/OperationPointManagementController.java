package vn.com.routex.hub.management.service.interfaces.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.hub.management.service.application.command.operationpoint.CreateOperationPointResult;
import vn.com.routex.hub.management.service.application.services.OperationPointManagementService;
import vn.com.routex.hub.management.service.interfaces.models.operationpoint.CreateOperationPointRequest;
import vn.com.routex.hub.management.service.interfaces.models.operationpoint.CreateOperationPointResponse;
import vn.com.routex.hub.management.service.interfaces.models.operationpoint.DeleteOperationPointRequest;
import vn.com.routex.hub.management.service.interfaces.models.operationpoint.DeleteOperationPointResponse;
import vn.com.routex.hub.management.service.interfaces.models.operationpoint.FetchOperationPointResponse;
import vn.com.routex.hub.management.service.interfaces.models.operationpoint.UpdateOperationPointRequest;
import vn.com.routex.hub.management.service.interfaces.models.operationpoint.UpdateOperationPointResponse;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.CREATE_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.DELETE_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.FETCH_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MANAGEMENT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.POINT_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.UPDATE_PATH;

@RestController
@RequestMapping(API_PATH + API_VERSION + MANAGEMENT_PATH)
@RequiredArgsConstructor
public class OperationPointManagementController {

    private final OperationPointManagementService operationPointManagementService;

    @PostMapping(POINT_PATH + CREATE_PATH)
    public ResponseEntity<CreateOperationPointResponse> createOperationPoint(@Valid @RequestBody CreateOperationPointRequest request) {

        return null;
    }

    @PostMapping(POINT_PATH + UPDATE_PATH)
    public ResponseEntity<UpdateOperationPointResponse> updateOperationPoint(@Valid @RequestBody UpdateOperationPointRequest request) {
        return null;
    }

    @PostMapping(POINT_PATH + DELETE_PATH)
    public ResponseEntity<DeleteOperationPointResponse> createOperationPoint(@Valid @RequestBody DeleteOperationPointRequest request) {
        return null;
    }

    @GetMapping(POINT_PATH + FETCH_PATH)
    public ResponseEntity<FetchOperationPointResponse> fetchOperationPoint(@RequestParam int pageNumber,
                                                                           @RequestParam int pageSize,
                                                                           HttpServletRequest servletRequest) {
        return null;
    }
}
