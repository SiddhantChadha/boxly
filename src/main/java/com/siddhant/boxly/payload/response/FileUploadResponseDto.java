package com.siddhant.boxly.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileUploadResponseDto {

    private String fileName;
    private boolean successful;


}
