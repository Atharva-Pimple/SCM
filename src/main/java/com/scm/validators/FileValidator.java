package com.scm.validators;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<ValidFile,MultipartFile>{

    private static final long MAX_FILE_SIZE=1024*1024*2;

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        
        // if(file.isEmpty() || file==null){
        //     context.disableDefaultConstraintViolation();
        //     context.buildConstraintViolationWithTemplate("File cannot be empty").addConstraintViolation();
        //     return false;
        // }
        // size
        if(file.getSize() > MAX_FILE_SIZE){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File cannot be larger than 2MB").addConstraintViolation();
            return false;
        }

        
        
        return true;
    }

}
