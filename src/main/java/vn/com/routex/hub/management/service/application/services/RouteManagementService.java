package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.command.route.AssignRouteCommand;
import vn.com.routex.hub.management.service.application.command.route.AssignRouteResult;
import vn.com.routex.hub.management.service.application.command.route.CreateRouteCommand;
import vn.com.routex.hub.management.service.application.command.route.CreateRouteResult;
import vn.com.routex.hub.management.service.application.command.route.DeleteRouteCommand;
import vn.com.routex.hub.management.service.application.command.route.DeleteRouteResult;
import vn.com.routex.hub.management.service.application.command.route.FetchRoutesQuery;
import vn.com.routex.hub.management.service.application.command.route.FetchRoutesResult;
import vn.com.routex.hub.management.service.application.command.route.SearchRouteQuery;
import vn.com.routex.hub.management.service.application.command.route.SearchRouteResult;
import vn.com.routex.hub.management.service.application.command.route.UpdateRouteCommand;
import vn.com.routex.hub.management.service.application.command.route.UpdateRouteResult;

public interface RouteManagementService {

    CreateRouteResult createRoute(CreateRouteCommand command);

    AssignRouteResult assignRoute(AssignRouteCommand command);

    UpdateRouteResult updateRoute(UpdateRouteCommand command);

    SearchRouteResult searchRoute(SearchRouteQuery query);

    FetchRoutesResult fetchRoutes(FetchRoutesQuery query);

    DeleteRouteResult deleteRoute(DeleteRouteCommand command);
}
