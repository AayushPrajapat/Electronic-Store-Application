package com.lcwd.electronic.store.validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid, String> {

	private static final Logger log = LoggerFactory.getLogger(ImageNameValidator.class);

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		log.info("Message from isValid : {} ", value);

//		logic
		if (value.isBlank()) {
			return false;
		}

		return true;
	}

}
