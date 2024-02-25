package com.siddhant.boxly.controllers;

import com.siddhant.boxly.payload.request.FileShareRequestDto;
import com.siddhant.boxly.payload.response.*;
import com.siddhant.boxly.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FileController {

    private FileService fileService;

    @Autowired
    public FileController(FileService fileService){
        this.fileService = fileService;
    }

    @PostMapping(value = "/files/upload",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse> addFiles(@RequestParam("files")MultipartFile[] files){
        List<FileUploadResponseDto> fileUploadResponseDtos =  fileService.uploadFiles(files);
        return new ResponseEntity<>(new ApiResponse(true, fileUploadResponseDtos), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}/files")
    @PreAuthorize("principal.getId() == #userId")
    public ResponseEntity<ApiResponse> getFiles(@PathVariable Integer userId){
        List<FileResponseDto> fileResponseDtoList = fileService.getAllFiles();
        return new ResponseEntity<>(new ApiResponse(true,fileResponseDtoList),HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/archive")
    @PreAuthorize("principal.getId() == #userId")
    public ResponseEntity<ApiResponse> getAllArchivedFiles(){
        List<FileResponseDto> fileResponseDtoList = fileService.getAllArchivedFiles();
        return new ResponseEntity<>(new ApiResponse(true,fileResponseDtoList),HttpStatus.OK);
    }

    @GetMapping("/files/{fileId}")
    @PreAuthorize("@authHelper.hasCreatedFile(#root,#fileId) or @authHelper.hasAccessToFile(#root,#fileId)")
    public ResponseEntity<ApiResponse> getFileDetails(@PathVariable Integer fileId){
        FileResponseDto fileResponseDto = fileService.getFileDetails(fileId);
        return new ResponseEntity<>(new ApiResponse(true,fileResponseDto),HttpStatus.OK);
    }

    @PutMapping("/files/{fileId}")
    @PreAuthorize("@authHelper.hasCreatedFile(#root,#fileId)")
    public ResponseEntity<ApiResponse> restoreFile(@PathVariable Integer fileId){
        FileResponseDto fileResponseDto = fileService.restoreFile(fileId);
        return new ResponseEntity<>(new ApiResponse(true,fileResponseDto),HttpStatus.OK);
    }

    @DeleteMapping("/files/{fileId}")
    @PreAuthorize("@authHelper.hasCreatedFile(#root,#fileId)")
    public ResponseEntity<ApiResponse> deleteFile(@PathVariable Integer fileId){
        fileService.deleteFile(fileId);
        return new ResponseEntity<>(new ApiResponse(true,"File deleted successfully"),HttpStatus.OK);
    }

    @PostMapping("/files/{fileId}/share")
    @PreAuthorize("@authHelper.hasCreatedFile(#root,#fileId)")
    public ResponseEntity<ApiResponse> shareFile(@PathVariable Integer fileId, @RequestBody FileShareRequestDto fileShareRequestDto){
        List<FileShareResponseDto> fileShareResponseDtoList = fileService.giveAccessToFile(fileId,fileShareRequestDto.getUserEmail());
        return new ResponseEntity<>(new ApiResponse(true,fileShareResponseDtoList),HttpStatus.OK);
    }

    @GetMapping("/files/{fileId}/users")
    @PreAuthorize("@authHelper.hasCreatedFile(#root,#fileId)")
    public ResponseEntity<ApiResponse> getUsersWithAccessToFile(@PathVariable Integer fileId){
        List<UserResponseDto> fileShareResponseDtoList = fileService.getUserWithAccessToFile(fileId);
        return new ResponseEntity<>(new ApiResponse(true,fileShareResponseDtoList),HttpStatus.OK);
    }

    @PutMapping("/files/{fileId}/users")
    @PreAuthorize("@authHelper.hasCreatedFile(#root,#fileId)")
    public ResponseEntity<ApiResponse> updateUsersAccessToFile(@PathVariable Integer fileId,@RequestBody FileShareRequestDto fileShareRequestDto){
        List<FileShareResponseDto> fileShareResponseDtoList = fileService.updateAccessToFile(fileId,fileShareRequestDto.getUserEmail());
        return new ResponseEntity<>(new ApiResponse(true,fileShareResponseDtoList),HttpStatus.OK);
    }
}
