package vn.com.routex.hub.user.service.application.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.user.service.application.services.VehicleManagementService;
import vn.com.routex.hub.user.service.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.hub.user.service.infrastructure.persistence.utils.ExceptionUtils;
import vn.com.routex.hub.user.service.interfaces.models.vehicle.AddVehicleRequest;
import vn.com.routex.hub.user.service.interfaces.models.vehicle.AddVehicleResponse;

import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.TIMEOUT_ERROR;
import static vn.com.routex.hub.user.service.infrastructure.persistence.constant.ErrorConstant.TIMEOUT_ERROR_MESSAGE;

@Component
@RequiredArgsConstructor
public class VehicleManagementFacadeImpl implements VehicleManagementFacade {

    private final VehicleManagementService vehicleManagementService;
    @Override
    public ResponseEntity<AddVehicleResponse> addVehicle(AddVehicleRequest request) {
        AddVehicleResponse response = vehicleManagementService.addVehicle(request);

        if(response == null) {
            throw new BusinessException(request.getRequestId(), request.getRequestDateTime(), request.getChannel(),
                    ExceptionUtils.buildResultResponse(TIMEOUT_ERROR, TIMEOUT_ERROR_MESSAGE));

        }
        response.setRequestId(request.getRequestId());
        response.setRequestDateTime(request.getRequestDateTime());
        response.setChannel(request.getChannel());

        if(response.getData() == null) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
