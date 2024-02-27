package com.siddhant.boxly.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDownloadResponseDto {

    private String fileName;

    private byte[] file;

}
