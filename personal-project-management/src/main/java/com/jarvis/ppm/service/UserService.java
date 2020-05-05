package com.jarvis.ppm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jarvis.ppm.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

}
