package com.siddhant.boxly.services.impl;

import com.siddhant.boxly.entities.File;
import com.siddhant.boxly.entities.User;
import com.siddhant.boxly.exceptions.FileUploadException;
import com.siddhant.boxly.exceptions.ResourceNotFoundException;
import com.siddhant.boxly.helper.FileUploadHelper;
import com.siddhant.boxly.payload.response.FileResponseDto;
import com.siddhant.boxly.payload.response.FileShareResponseDto;
import com.siddhant.boxly.payload.response.FileUploadResponseDto;
import com.siddhant.boxly.payload.response.UserResponseDto;
import com.siddhant.boxly.repositories.FileRepository;
import com.siddhant.boxly.repositories.UserRepository;
import com.siddhant.boxly.services.FileService;
import com.siddhant.boxly.utils.Status;
import com.siddhant.boxly.utils.StorageBucketUtil;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private FileRepository fileRepository;
    private UserRepository userRepository;
    private StorageBucketUtil storageBucketUtil;

    private ModelMapper modelMapper;

    private FileUploadHelper fileUploadHelper;


    public FileServiceImpl(FileRepository fileRepository, UserRepository userRepository, StorageBucketUtil storageBucketUtil, ModelMapper modelMapper, FileUploadHelper fileUploadHelper){
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.storageBucketUtil = storageBucketUtil;
        this.modelMapper = modelMapper;
        this.fileUploadHelper = fileUploadHelper;
    }


    @Override
    public List<FileUploadResponseDto> uploadFiles(MultipartFile[] files){

        List<CompletableFuture<FileUploadResponseDto>> futures =  Arrays.stream(files).map(file-> fileUploadHelper.uploadFileHelper(file)).toList();
        CompletableFuture allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        CompletableFuture<List<FileUploadResponseDto>> completedListOfDto =  allFutures.thenApply(future-> futures.stream().map(f->f.join()).collect(Collectors.toList()));

        try {
            return completedListOfDto.get();
        } catch (Exception e){
            throw new FileUploadException("Some issued occured while uploading file");
        }
    }

    @Override
    public void deleteFile(Integer fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(()->new ResourceNotFoundException("File","id",fileId));
        file.setStatus(Status.DELETED);
        fileRepository.save(file);
    }

    @Override
    public FileResponseDto restoreFile(Integer fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(()->new ResourceNotFoundException("File","id",fileId));
        file.setStatus(Status.LIVE);
        File updatedFile = fileRepository.save(file);
        return modelMapper.map(updatedFile,FileResponseDto.class) ;
    }

    @Override
    public FileResponseDto getFileDetails(Integer fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(()->new ResourceNotFoundException("File","id",fileId));
        FileResponseDto fileResponseDto = modelMapper.map(file,FileResponseDto.class);
        return fileResponseDto;
    }

    @Override
    public List<FileResponseDto> getAllArchivedFiles() {
        List<File> fileList = fileRepository.findByCreatedByAndStatus((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),Status.DELETED);
        return fileList.stream().map(file->modelMapper.map(file,FileResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<FileResponseDto> getAllFiles() {
        List<File> fileList = fileRepository.findByCreatedByAndStatus((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),Status.LIVE);
        return fileList.stream().map(file->modelMapper.map(file,FileResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<FileShareResponseDto> giveAccessToFile(Integer fileId,List<String> userEmail){
        File file = fileRepository.findById(fileId).orElseThrow(()->new ResourceNotFoundException("File","id",fileId));
        List<FileShareResponseDto> fileShareResponseDtoList = userEmail.stream().map(userId->{
            Optional<User> optionalUser = userRepository.findByEmail(userId);

            if(optionalUser.isPresent()){
                User user = optionalUser.get();

                if(!file.getSharedWith().contains(user)){
                    file.getSharedWith().add(user);
                }

                return FileShareResponseDto.builder().shared(true).userEmail(userId).build();
            }else {
                return FileShareResponseDto.builder().shared(false).userEmail(userId).build();
            }
        }).toList();

        fileRepository.save(file);
        return fileShareResponseDtoList;
    }

    @Override
    public List<UserResponseDto> getUserWithAccessToFile(Integer fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(()->new ResourceNotFoundException("File","id",fileId));
        return file.getSharedWith().stream().map(user-> modelMapper.map(user,UserResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<FileShareResponseDto> updateAccessToFile(Integer fileId, List<String> userEmail) {
        File file = fileRepository.findById(fileId).orElseThrow(()->new ResourceNotFoundException("File","id",fileId));

        file.getSharedWith().clear();

        List<FileShareResponseDto> fileShareResponseDtoList = userEmail.stream().map(email->{
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if(optionalUser.isPresent()){
                User user = optionalUser.get();
                file.getSharedWith().add(user);

                return FileShareResponseDto.builder().shared(true).userEmail(user.getEmail()).build();
            }else{
                return FileShareResponseDto.builder().shared(false).userEmail(email).build();
            }

        }).toList();


        return fileShareResponseDtoList;
    }

}
