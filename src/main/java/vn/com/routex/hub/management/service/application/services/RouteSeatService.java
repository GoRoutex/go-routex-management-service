package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.command.seat.SearchSeatCommand;
import vn.com.routex.hub.management.service.application.command.seat.SearchSeatResult;

public interface RouteSeatService {

    SearchSeatResult searchSeat(SearchSeatCommand command);
}
