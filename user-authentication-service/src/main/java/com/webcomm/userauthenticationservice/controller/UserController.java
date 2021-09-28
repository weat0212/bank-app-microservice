package com.webcomm.userauthenticationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.webcomm.userauthenticationservice.model.User;
import com.webcomm.userauthenticationservice.service.UserService;
import com.webcomm.userauthenticationservice.web.dto.JwtDto;
import com.webcomm.userauthenticationservice.web.dto.UserDto;


@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
//	@GetMapping("/")
//    public String root() {
//        return "index";
//    }

//    @GetMapping("/login")
//    public String login(Model model) {
//        return "login";
//    }
	
//    @GetMapping("/user")
//    public String userIndex() {
//        return "user/index";
//    }

    @GetMapping("/test")
    public @ResponseBody String test() {
        return "{\"test\":\"test\"}";
    }
    
    @GetMapping("/userinfo")
    public UserDto getUser(@RequestHeader("Authorization") String jwt) {
    	
    	String jwtToken = jwt.replace("Bearer ", "");
    	
    	User user = userService.findByJwtToken(jwtToken);
    	
    	if(user == null) { throw new NullPointerException(); }
    	else {
    		UserDto returnUser = new UserDto();
    		returnUser.setId(Long.toString(user.getId()));
    		returnUser.setEmail(user.getEmail());
    		returnUser.setFirstName(user.getFirstName());
    		returnUser.setLastName(user.getLastName());
    		return returnUser;
    	}
    }
}
