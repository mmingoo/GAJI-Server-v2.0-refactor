package gaji.service.domain.file.mapper;

import gaji.service.domain.file.domain.Files;
import gaji.service.global.exception.RestApiException;
import gaji.service.global.exception.code.status.GlobalErrorStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileMapper {

    public Files toEntity(MultipartFile file, String imageUrl) {
        try {
            return Files.builder()
                    .type(file.getContentType())
                    .size((int) file.getSize())
                    .originalName(file.getOriginalFilename())
                    .fileName(file.getName())
                    .path(imageUrl)
                    .build();
        } catch (Exception e) {
            throw new RestApiException(GlobalErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }
}
