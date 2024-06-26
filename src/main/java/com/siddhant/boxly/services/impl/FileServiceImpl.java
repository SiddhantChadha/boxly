package com.siddhant.boxly.services.impl;

import com.siddhant.boxly.entities.File;
import com.siddhant.boxly.entities.User;
import com.siddhant.boxly.exceptions.FileUploadException;
import com.siddhant.boxly.exceptions.ResourceNotFoundException;
import com.siddhant.boxly.helper.FileUploadHelper;
import com.siddhant.boxly.payload.response.*;
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
    private ModelMapper modelMapper;
    private FileUploadHelper fileUploadHelper;

    private StorageBucketUtil storageBucketUtil;

    public FileServiceImpl(FileRepository fileRepository, UserRepository userRepository, ModelMapper modelMapper, FileUploadHelper fileUploadHelper, StorageBucketUtil storageBucketUtil){
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.fileUploadHelper = fileUploadHelper;
        this.storageBucketUtil = storageBucketUtil;
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
    public List<FileResponseDto> getAllSharedFiles() {
        List<File> fileList = fileRepository.findBySharedWithAndStatus((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),Status.LIVE);
        return fileList.stream().map(file->modelMapper.map(file,FileResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getUserWithAccessToFile(Integer fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(()->new ResourceNotFoundException("File","id",fileId));
        return file.getSharedWith().stream().map(user-> modelMapper.map(user,UserResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<FileShareResponseDto> updateAccessToFile(Integer fileId, List<String> addUser,List<String> removeUser) {
        File file = fileRepository.findById(fileId).orElseThrow(()->new ResourceNotFoundException("File","id",fileId));

        removeUser.forEach((email)->{
            Optional<User> user = userRepository.findByEmail(email);
            if(user.isPresent()){

                file.getSharedWith().remove(user.get());
            }
        });

        List<FileShareResponseDto> fileShareResponseDtoList = addUser.stream().map(email->{
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if(optionalUser.isPresent()){
                User user = optionalUser.get();
                file.getSharedWith().add(user);

                return FileShareResponseDto.builder().shared(true).userEmail(user.getEmail()).build();
            }else{
                return FileShareResponseDto.builder().shared(false).userEmail(email).build();
            }

        }).toList();
        fileRepository.save(file);

        return fileShareResponseDtoList;
    }

    @Override
    public FileDownloadResponseDto downloadFile(Integer fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(()->new ResourceNotFoundException("File","id",fileId));
        byte[] fileBytes = storageBucketUtil.downloadFile(file.getGeneratedName());
        return FileDownloadResponseDto.builder().fileName(file.getOriginalName()).file(fileBytes).build();
    }

    public FileResponseDto renameFile(Integer fileId,String newName){
        File file = fileRepository.findById(fileId).orElseThrow(()->new ResourceNotFoundException("File","id",fileId));
        String extension = file.getOriginalName().substring(file.getOriginalName().lastIndexOf('.'));
        file.setOriginalName(newName + extension);
        File updatedFile = fileRepository.save(file);
        return modelMapper.map(updatedFile,FileResponseDto.class);
    }

}
