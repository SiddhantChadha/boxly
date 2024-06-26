package com.siddhant.boxly.services;

import com.siddhant.boxly.payload.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    
    List<FileUploadResponseDto> uploadFiles(MultipartFile[] multipartFiles);
    void deleteFile(Integer fileId);
    FileResponseDto restoreFile(Integer fileId);

    FileResponseDto getFileDetails(Integer fileId);

    List<FileResponseDto> getAllArchivedFiles();
    List<FileResponseDto> getAllFiles();

    List<FileResponseDto> getAllSharedFiles();

    List<UserResponseDto> getUserWithAccessToFile(Integer fileId);

    List<FileShareResponseDto> updateAccessToFile(Integer fileId,List<String> addUser,List<String> removeUser);

    FileDownloadResponseDto downloadFile(Integer fileId);

    FileResponseDto renameFile(Integer fileId,String newName);
}
