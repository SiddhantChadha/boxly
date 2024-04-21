package com.siddhant.boxly.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class FileShareRequestDto {

    private List<String> addUser;
    private List<String> removeUser;

}
