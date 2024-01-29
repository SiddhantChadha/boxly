package com.siddhant.boxly.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResponse <T>{

    private boolean success;
    private T data;
}
