package com.siddhant.boxly.helper;

import com.siddhant.boxly.entities.File;
import com.siddhant.boxly.entities.User;
import com.siddhant.boxly.payload.request.FileRequestDto;
import com.siddhant.boxly.payload.response.FileUploadResponseDto;
import com.siddhant.boxly.payload.response.UploadedFileDto;
import com.siddhant.boxly.repositories.FileRepository;
import com.siddhant.boxly.utils.Status;
import com.siddhant.boxly.utils.StorageBucketUtil;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@Component
public class FileUploadHelper {
    private FileRepository fileRepository;
    private StorageBucketUtil storageBucketUtil;
    private ModelMapper modelMapper;

    public FileUploadHelper(FileRepository fileRepository, StorageBucketUtil storageBucketUtil, ModelMapper modelMapper){
        this.fileRepository = fileRepository;
        this.storageBucketUtil = storageBucketUtil;
        this.modelMapper = modelMapper;
    }

    @Async
    public CompletableFuture<FileUploadResponseDto> uploadFileHelper(MultipartFile multipartFile) {

        try {
            UploadedFileDto uploadedFileDto = storageBucketUtil.uploadFile(multipartFile);
            FileRequestDto fileRequestDto = FileRequestDto.builder()
                    .size(uploadedFileDto.getSize()).status(Status.LIVE)
                    .generatedName(uploadedFileDto.getFileName())
                    .originalName(multipartFile.getOriginalFilename())
                    .thumbnailUrl("https://e7.pngegg.com/pngimages/521/255/png-clipart-computer-icons-data-file-document-file-format-others-miscellaneous-blue.png")
                    .createdBy((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).build();


            File file = modelMapper.map(fileRequestDto, File.class);
            fileRepository.save(file);
            return CompletableFuture.completedFuture(new FileUploadResponseDto(multipartFile.getOriginalFilename(), true));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return CompletableFuture.completedFuture(new FileUploadResponseDto(multipartFile.getOriginalFilename(),false));
        }
    }


}
