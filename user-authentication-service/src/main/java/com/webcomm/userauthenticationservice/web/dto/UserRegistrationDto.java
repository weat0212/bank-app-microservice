package com.webcomm.userauthenticationservice.web.dto;


import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserRegistrationDto {

	@NotNull(message = "First Name cannot be null")
	@NotBlank
    private String firstName;

	@NotNull(message = "Last Name cannot be null")
	@NotBlank
    private String lastName;

	@NotNull(message = "Password cannot be null")
	@NotBlank
	@Pattern(regexp="^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[a-zA-Z]).{3,32}$", 
			message="At least one letter, one number, and one symbol")
    private String password;

    @NotEmpty
    @NotBlank
    private String confirmPassword;

    @Email(message = "Email should be valid")
    @NotBlank
    @NotEmpty
    private String email;

    @Email
    @NotEmpty
    @NotBlank
    private String confirmEmail;

    @AssertTrue(message = "Agree with the terms and conditions for Registration")
    private Boolean terms;
}
