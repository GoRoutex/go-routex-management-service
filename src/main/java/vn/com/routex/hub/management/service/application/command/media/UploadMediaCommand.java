package vn.com.routex.hub.management.service.application.command.media;


import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;
import vn.com.routex.hub.management.service.application.command.common.RequestContext;

@Builder
public record UploadMediaCommand(
        RequestContext context,
        MultipartFile file,
        String folder,
        String publicId
) {
}
