package com.siddhant.boxly.payload.request;

import com.siddhant.boxly.entities.User;
import com.siddhant.boxly.utils.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class FileRequestDto {

    private String generatedName;
    private String originalName;
    private String thumbnailUrl;
    private Long size;
    private Status status;
    private User createdBy;
    private LocalDateTime createdAt;

}
