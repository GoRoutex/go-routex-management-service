package vn.com.routex.hub.management.service.application.services;


import vn.com.routex.hub.management.service.application.command.user.GetUserProfileCommand;
import vn.com.routex.hub.management.service.application.command.user.GetUserProfileResult;

public interface UserProfileService {

    GetUserProfileResult getUserProfile(GetUserProfileCommand command);
}
