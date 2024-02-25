package com.siddhant.boxly.services;

import com.siddhant.boxly.payload.response.FileResponseDto;
import com.siddhant.boxly.payload.response.FileShareResponseDto;
import com.siddhant.boxly.payload.response.FileUploadResponseDto;
import com.siddhant.boxly.payload.response.UserResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    
    List<FileUploadResponseDto> uploadFiles(MultipartFile[] multipartFiles);
    void deleteFile(Integer fileId);
    FileResponseDto restoreFile(Integer fileId);

    FileResponseDto getFileDetails(Integer fileId);

    List<FileResponseDto> getAllArchivedFiles();
    List<FileResponseDto> getAllFiles();
    List<FileShareResponseDto> giveAccessToFile(Integer fileId,List<String> userEmail);

    List<UserResponseDto> getUserWithAccessToFile(Integer fileId);

    List<FileShareResponseDto> updateAccessToFile(Integer fileId,List<String> userEmail);
}
