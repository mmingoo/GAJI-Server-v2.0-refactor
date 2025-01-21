package gaji.service.domain.file.service;

import gaji.service.domain.file.dto.response.FileCreateResponse;
import gaji.service.domain.file.dto.response.FileDeleteResponse;
import gaji.service.global.enums.FileCategory;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    FileCreateResponse createFile(FileCategory fIleCategory , final MultipartFile file);

    FileDeleteResponse deleteFile(String fileUrl);
}