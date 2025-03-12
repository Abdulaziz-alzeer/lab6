package com.example.lab_6.Model;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;


@Data
@AllArgsConstructor
public class Employee {


    @NotEmpty(message = "Id should be not empty")
    @Size(min = 3,message = "length of ID should be bigger than 2 ")
    private String ID;

    @NotEmpty(message = "name should be not empty")
    @Size(min = 5,message = "length of name should be bigger than 4")
    @Pattern(regexp = "^[a-zA-Z ]+$",message = "name should be contain only characters (no numbers)")
    private String name;

    @Email
    @NotEmpty(message = "email should be not empty")
    private String  email;

    @Pattern(regexp = "^05\\d{8}$",message = "phone number must start with '05' and must consists exactly 10 digits. ")
    @NotEmpty(message = "Phone number should be not empty")
    private String phoneNumber;

    @NotNull(message = "age must be not null")
    @Min(value = 26,message = "age must be more than 25")
    private int age;

    @NotEmpty(message = "position should be not empty")
    @Pattern(regexp = "^(supervisor|coordinator)$" ,message = "position must be either 'supervisor' or 'coordinator' only")
    private String position;

    @AssertFalse(message = "on leave must be initially set to false ")
    private boolean onLeave;

    @NotNull(message = "hire date should be not empty")
    @PastOrPresent(message = "the hire date should be in the present or the past. ")
    private Date hireDate;

    @NotNull(message = "annual leave should be not null")
    @Min(value = 1,message = "annual must be a positive number")
    private int annualLeave;

}
