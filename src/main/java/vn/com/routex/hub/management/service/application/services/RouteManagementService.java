package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.dto.route.AssignRouteCommand;
import vn.com.routex.hub.management.service.application.dto.route.AssignRouteResult;
import vn.com.routex.hub.management.service.application.dto.route.CreateRouteCommand;
import vn.com.routex.hub.management.service.application.dto.route.CreateRouteResult;
import vn.com.routex.hub.management.service.application.dto.route.DeleteRouteCommand;
import vn.com.routex.hub.management.service.application.dto.route.DeleteRouteResult;
import vn.com.routex.hub.management.service.application.dto.route.FetchRouteQuery;
import vn.com.routex.hub.management.service.application.dto.route.FetchRouteResult;
import vn.com.routex.hub.management.service.application.dto.route.SearchRouteQuery;
import vn.com.routex.hub.management.service.application.dto.route.SearchRouteResult;

public interface RouteManagementService {

    CreateRouteResult createRoute(CreateRouteCommand command);

    AssignRouteResult assignRoute(AssignRouteCommand command);

    SearchRouteResult searchRoute(SearchRouteQuery query);

    FetchRouteResult fetchRoute(FetchRouteQuery query);

    DeleteRouteResult deleteRoute(DeleteRouteCommand command);
}
