package com.example.demo.validator;

import com.example.demo.entity.Member;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.validation.constraints.Size;

public class MemberValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Member.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Member member = (Member)target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "uid", "NotEmpty");
        if(member.getUid().length() < 3 || member.getUid().length() > 32){
            errors.rejectValue("uid", "Size.memberForm.uid");
        }
    }
}
