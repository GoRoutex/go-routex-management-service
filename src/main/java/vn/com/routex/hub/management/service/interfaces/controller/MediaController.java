package vn.com.routex.hub.management.service.interfaces.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.hub.management.service.application.command.media.UploadMediaCommand;
import vn.com.routex.hub.management.service.application.command.media.UploadMediaResult;
import vn.com.routex.hub.management.service.application.services.MediaService;
import vn.com.routex.hub.management.service.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.hub.management.service.interfaces.factory.ApiResultFactory;
import vn.com.routex.hub.management.service.interfaces.models.media.UploadMediaRequest;
import vn.com.routex.hub.management.service.interfaces.models.media.UploadMediaResponse;

import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.MEDIA_PATH;
import static vn.com.routex.hub.management.service.infrastructure.persistence.constant.ApiConstant.UPLOAD;

@RestController
@RequestMapping(API_PATH + API_VERSION + MEDIA_PATH)
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());
    private final ApiResultFactory apiResultFactory;

    @PostMapping(path = UPLOAD, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadMediaResponse> uploadMedia(@Valid @ModelAttribute UploadMediaRequest request) {

        sLog.info("[MEDIA-UPLOAD] Upload Media Request: {}", request);
        UploadMediaCommand command = UploadMediaCommand
                .builder()
                .context(HttpUtils.toContext(request))
                .file(request.getData().getFile())
                .folder(request.getData().getFolder())
                .publicId(request.getData().getPublicId())
                .build();

        UploadMediaResult result = mediaService.uploadMedia(command);


        UploadMediaResponse response = UploadMediaResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(UploadMediaResponse.UploadMediaResponseData.builder()
                        .publicId(result.publicId())
                        .originalFilename(result.originalFilename())
                        .resourceType(result.resourceType())
                        .format(result.format())
                        .url(result.url())
                        .secureUrl(result.secureUrl())
                        .folder(result.folder())
                        .bytes(result.bytes())
                        .width(result.width())
                        .height(result.height())
                        .build())
                .build();

        sLog.info("[MEDIA-UPLOAD] Upload Media Response: {}", request);

        return HttpUtils.buildResponse(request, response);
    }
}
