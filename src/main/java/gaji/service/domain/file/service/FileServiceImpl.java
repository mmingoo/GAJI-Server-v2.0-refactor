package gaji.service.domain.file.service;

import gaji.service.aws.s3.AmazonS3Manager;
import gaji.service.domain.file.domain.Files;
import gaji.service.domain.file.dto.response.FileCreateResponse;
import gaji.service.domain.file.dto.response.FileDeleteResponse;
import gaji.service.domain.file.mapper.FileMapper;
import gaji.service.domain.file.repository.FileRepository;
import gaji.service.global.enums.FileCategory;
import gaji.service.global.exception.RestApiException;
import gaji.service.global.exception.code.status.FilesErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileServiceImpl implements FileService {

    private final AmazonS3Manager amazonS3Manager;
    private final FileRepository filesRepository;
    private final FileMapper filesMapper;

    @Override
    @Transactional
    public FileCreateResponse createFile(FileCategory fIleCategory, final MultipartFile file) {
        String imageUrl = amazonS3Manager.uploadFile(fIleCategory.getCategory(), file);
        // 파일 정보 추출 및 엔티티 생성
        Files filesEntity = filesMapper.toEntity(file, imageUrl);

        // 파일 정보 저장
        filesRepository.save(filesEntity);

        return new FileCreateResponse(imageUrl);
    }

    @Override
    @Transactional
    public FileDeleteResponse deleteFile(String fileUrl) {
        Files files = filesRepository.findByPath(fileUrl);

        if (files == null) {
            throw new RestApiException(FilesErrorStatus._NOT_FOUND_FILE_URL); // 파일이 존재하지 않을 경우 예외 처리
        }

        filesRepository.delete(files);
        amazonS3Manager.deleteFile(fileUrl);

        return new FileDeleteResponse(fileUrl);
    }
}
