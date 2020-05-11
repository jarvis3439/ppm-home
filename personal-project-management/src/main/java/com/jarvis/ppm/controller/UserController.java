package com.jarvis.ppm.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jarvis.ppm.UserValidator;
import com.jarvis.ppm.model.User;
import com.jarvis.ppm.payload.JWTLoginSuccessResponse;
import com.jarvis.ppm.payload.LoginRequest;
import com.jarvis.ppm.security.JwtTokenProvider;
import com.jarvis.ppm.service.ErrorValidationService;
import com.jarvis.ppm.service.UserService;

import static com.jarvis.ppm.security.SecurityConstraints.TOKEN_PREFIX;;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ErrorValidationService errorValidationService;

	@Autowired
	private UserValidator userValidator;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {
		ResponseEntity<?> errorMap = errorValidationService.validationService(result);
		if (errorMap != null) {
			return errorMap;
		}

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);

		return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {
		userValidator.validate(user, result);
		ResponseEntity<?> errorMap = errorValidationService.validationService(result);
		if (errorMap != null) {
			return errorMap;
		}
		User newUser = userService.saveUser(user);
		return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
	}

}
