package com.siddhant.boxly.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileShareResponseDto {

    private String userEmail;
    private boolean shared;
}
