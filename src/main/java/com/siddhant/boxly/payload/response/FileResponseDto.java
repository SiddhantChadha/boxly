package com.siddhant.boxly.payload.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileResponseDto {

    private Integer id;
    private String originalName;

    private Long size;

    private LocalDateTime createdAt;

    private UserResponseDto createdBy;

}
