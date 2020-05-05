package com.jarvis.ppm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jarvis.ppm.model.User;

@Component
public class UserValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;

		// confirm password validation
		if (user.getPassword() != null) {
			if (!user.getPassword().equals(user.getConfirmPassword())) {
				errors.rejectValue("confirmPassword", "Match", "Password must be same");
			}
		}
	}

}
