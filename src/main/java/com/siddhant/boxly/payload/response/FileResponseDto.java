package com.siddhant.boxly.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileResponseDto {

    private Integer id;
    private String originalName;
    private String thumbnailUrl;
    private Long size;

    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:SS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    private UserResponseDto createdBy;

}
