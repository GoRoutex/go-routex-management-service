package vn.com.routex.hub.management.service.application.command.media;


import lombok.Builder;

@Builder
public record UploadMediaResult(
        String publicId,
        String originalFilename,
        String resourceType,
        String format,
        String url,
        String secureUrl,
        String folder,
        Long bytes,
        Integer width,
        Integer height
) {
}
