package com.jwt.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.demo.config.JwtTokenUtil;
import com.jwt.demo.model.ApiResponse;
import com.jwt.demo.model.AuthToken;
import com.jwt.demo.model.LoginUser;
import com.jwt.demo.model.User;
import com.jwt.demo.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/token")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserService userService;

	/*@RequestMapping(value = "/generate-token", method = RequestMethod.POST)
	public ApiResponse<AuthToken> register(@RequestBody LoginUser loginUser) throws AuthenticationException {

		System.out.println("Hi there i changed prop am here!! resjkstarting i am changing stuffs heres");
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
		final User user = userService.findOne(loginUser.getUsername());
		final String token = jwtTokenUtil.generateToken(user);
		return new ApiResponse<>(200, "success", new AuthToken(token, user.getUsername()));
	}*/
	
	@RequestMapping(value = "/generate-token", method = RequestMethod.POST)
	public ApiResponse<AuthToken> register(@RequestBody LoginUser loginUser,HttpServletRequest request) throws AuthenticationException {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
		final User user = userService.findOne(loginUser.getUsername());
		 String token = jwtTokenUtil.generateToken(loginUser.getUsername(),request);
		 System.out.println("Username " +loginUser.getUsername() );
		 return new ApiResponse<>(200, "success", new AuthToken(token, user.getUsername()));
	}

}
