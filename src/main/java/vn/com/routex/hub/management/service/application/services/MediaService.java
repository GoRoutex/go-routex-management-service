package vn.com.routex.hub.management.service.application.services;

import vn.com.routex.hub.management.service.application.command.media.UploadMediaCommand;
import vn.com.routex.hub.management.service.application.command.media.UploadMediaResult;

public interface MediaService {
    UploadMediaResult uploadMedia(UploadMediaCommand command);
}
