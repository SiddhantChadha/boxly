package com.siddhant.boxly.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequestDto {
    @Email(message = "Email address is not valid")
    private String email;

    @NotEmpty
    @Size(min = 4,max = 10,message = "Password must be 4 to 10 characters long")
    private String password;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

}
