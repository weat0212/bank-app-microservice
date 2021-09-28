package com.webcomm.userauthenticationservice.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.webcomm.userauthenticationservice.model.User;
import com.webcomm.userauthenticationservice.service.UserService;
import com.webcomm.userauthenticationservice.web.dto.UserDto;
import com.webcomm.userauthenticationservice.web.dto.UserRegistrationDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "registration";
    }
    
    @PostMapping()
    @ResponseBody
    public UserDto userRegistry(@RequestBody UserRegistrationDto user) {
    	UserDto returnUser = new UserDto();
    	returnUser.setEmail(user.getEmail());
    	returnUser.setFirstName(user.getFirstName());
    	returnUser.setLastName(user.getLastName());
    	userService.save(user);
    	return returnUser;  
	}

    @PostMapping("/form")
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result){

        User existing = userService.findByEmail(userDto.getEmail());
        if (existing != null){
            result.rejectValue("email", null, "There is already an account registered with that email");
        }

        if (!StringUtils.hasLength(userDto.getFirstName())) {
            result.rejectValue("firstName", null, "First Name blank not filled");
        }

        if (!StringUtils.hasLength(userDto.getLastName())) {
            result.rejectValue("lastName", null, "Last Name blank not filled");
        }

        if (userDto.getEmail() == null || !userDto.getEmail().equals(userDto.getConfirmEmail())) {
            result.rejectValue("confirmEmail", null, "Email Error!");
        }

        if (userDto.getPassword() == null || !userDto.getPassword().equals(userDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", null, "Password Error!");
        }

        if (!userDto.getTerms()) {result.rejectValue("", null, "");}

        if (result.hasErrors()){
            return "registration";
        } else {
            userService.save(userDto);
            log.debug("New Registration");
            return "redirect:/registration?success";
        }
    }

    @GetMapping(value = "/username")
    @ResponseBody
    public String currentUserName(Authentication authentication, Model model) {

        String userName = authentication.getName();
        model.addAttribute("userName",userName);
        return "";
    }
    
    @GetMapping("/test")
    public @ResponseBody String test() {
        return "This is a Test in /registration";
    }

}