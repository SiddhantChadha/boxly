package com.siddhant.boxly.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UploadedFileDto {

    private String fileName;
    private Long size;
}
