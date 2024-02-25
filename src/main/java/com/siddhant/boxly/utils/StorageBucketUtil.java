package com.siddhant.boxly.utils;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.siddhant.boxly.payload.response.UploadedFileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class StorageBucketUtil {

    private Storage storage;

    @Value("${gcp.bucket.id}")
    private String gcpBucketId;

    public StorageBucketUtil(Storage storage){
        this.storage = storage;
    }

    public UploadedFileDto uploadFile(MultipartFile multipartFile) throws IOException {
            byte[] fileData = multipartFile.getBytes();
            Bucket bucket = storage.get(gcpBucketId,Storage.BucketGetOption.fields());
            String fileName = UUID.randomUUID().toString();

            Blob blob = bucket.create( fileName,fileData);

            return new UploadedFileDto(blob.getName(),blob.getSize());
    }

    public void downloadFile(String generationId){

        byte[] content = storage.readAllBytes(gcpBucketId,generationId);
        System.out.println(new String(content, StandardCharsets.UTF_8));
    }


}
