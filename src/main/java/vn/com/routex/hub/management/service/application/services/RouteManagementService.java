package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.command.route.AssignRouteCommand;
import vn.com.routex.hub.management.service.application.command.route.AssignRouteResult;
import vn.com.routex.hub.management.service.application.command.route.CreateRouteCommand;
import vn.com.routex.hub.management.service.application.command.route.CreateRouteResult;
import vn.com.routex.hub.management.service.application.command.route.DeleteRouteCommand;
import vn.com.routex.hub.management.service.application.command.route.DeleteRouteResult;
import vn.com.routex.hub.management.service.application.command.route.FetchRouteQuery;
import vn.com.routex.hub.management.service.application.command.route.FetchRouteResult;
import vn.com.routex.hub.management.service.application.command.route.SearchRouteQuery;
import vn.com.routex.hub.management.service.application.command.route.SearchRouteResult;

public interface RouteManagementService {

    CreateRouteResult createRoute(CreateRouteCommand command);

    AssignRouteResult assignRoute(AssignRouteCommand command);

    SearchRouteResult searchRoute(SearchRouteQuery query);

    FetchRouteResult fetchRoute(FetchRouteQuery query);

    DeleteRouteResult deleteRoute(DeleteRouteCommand command);
}
