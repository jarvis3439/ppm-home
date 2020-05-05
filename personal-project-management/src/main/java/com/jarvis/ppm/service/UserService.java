package com.jarvis.ppm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jarvis.ppm.exception.UserAlreadyExistException;
import com.jarvis.ppm.model.User;
import com.jarvis.ppm.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public User saveUser(User newUser) {
		try {
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			return userRepository.save(newUser);
		} catch (Exception e) {
			throw new UserAlreadyExistException("Username '" + newUser.getUsername() + "' already exists ");
		}

	}
}
